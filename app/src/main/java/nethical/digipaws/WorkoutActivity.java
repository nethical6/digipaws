package nethical.digipaws;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import androidx.core.graphics.ColorUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.internal.EdgeToEdgeUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.java.posedetector.PoseDetectorProcessor;
import com.google.mlkit.vision.demo.java.posedetector.classification.PoseClassifierProcessor;

import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nethical.digipaws.utils.CoinManager;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;

public class WorkoutActivity extends AppCompatActivity
        implements PoseDetectorProcessor.PoseDetectorListener {

    private PreviewView previewView;
    private GraphicOverlay graphicOverlay;
    private Button startButton;

    private ExecutorService cameraExecutor;
    private ExecutorService executor;
    private PoseDetectorProcessor imageProcessor;

    private boolean needUpdateGraphicOverlayImageSourceInfo = true;

    private int lensFacing = CameraSelector.LENS_FACING_FRONT;
    private ProcessCameraProvider cameraProvider;

    private Handler uiHandler;
    private boolean isQuestRunning = false;
    private CameraSelector cameraSelector;
    
    private String workoutType = PoseClassifierProcessor.SQUATS_CLASS;
    private int reps_final_count = DigiConstants.DEFAULT_REPS;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeUtils.applyEdgeToEdge(getWindow(),true);
    
        setContentView(R.layout.quest_workout);

        previewView = findViewById(R.id.previewView);
        graphicOverlay = findViewById(R.id.overlayView);
        startButton = findViewById(R.id.start_button_workout);
        sp = getSharedPreferences("workoutData",Context.MODE_PRIVATE);
        cameraExecutor = Executors.newSingleThreadExecutor();
        executor = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        if(intent!=null){
           workoutType = intent.getStringExtra(DigiConstants.KEY_WORKOUT_TYPE);
        }
        TextView title = findViewById(R.id.title);
        TextView desc = findViewById(R.id.desc);
        
        String description = getString(R.string.pushups_desc);
        switch(workoutType){
            case PoseClassifierProcessor.PUSHUPS_CLASS:
                title.setText("Quest: Pushups");
                reps_final_count = sp.getInt(PoseClassifierProcessor.PUSHUPS_CLASS,3)+2;
                description = description.replace("$value",String.valueOf(reps_final_count));
                desc.setText(description);
                break;
            case PoseClassifierProcessor.SQUATS_CLASS:
                description = getString(R.string.squats_desc);
                title.setText("Quest: Squats");
                reps_final_count = sp.getInt(PoseClassifierProcessor.SQUATS_CLASS,3)+2;
                description = description.replace("$value",String.valueOf(reps_final_count));
                desc.setText(description);
                break;
        }
        
        
        ImageView switchCamera = findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener((v)->{
            switchCameraLens();
        });
        
        MaterialCardView cardView = findViewById(R.id.bottom_card);
     
        ColorStateList backgroundColor = cardView.getCardBackgroundColor();
        int defaultColor = Color.WHITE; // Default color in case background color is null
        
        // Get the default background color of the CardView
        int backgroundColorValue = backgroundColor != null ? backgroundColor.getDefaultColor() : defaultColor;
        
        // Adjust the alpha to create a semi-transparent color (65% transparency)
        int semiTransparentColor = ColorUtils.setAlphaComponent(backgroundColorValue, (int) (0.65 * 255));

        // Find the CardView and set the background color
       cardView.setCardBackgroundColor(semiTransparentColor);
   
        
        
        startButton.setEnabled(false);

        uiHandler = new Handler(Looper.getMainLooper());
        
        checkCameraPermission();
        startCamera();
        loadCsvData();

        startButton.setOnClickListener(
                (v) -> {
                    if (startButton.isEnabled()) {
                        if(isQuestRunning){
                            finish();
                        }
                        startCamera();
                        startButton.setText(getString(R.string.stop));
                        isQuestRunning = true;
                    } else {
                        Toast.makeText(this, "Please wait while the AI loads", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(
                () -> {
                    try {
                        if(cameraProvider==null){
                            cameraProvider = cameraProviderFuture.get();
                        }
                        
                        Preview preview = new Preview.Builder().build();
                        preview.setSurfaceProvider(previewView.getSurfaceProvider());

                        if(cameraSelector==null){
                           cameraSelector =
                                new CameraSelector.Builder()
                                        .requireLensFacing(lensFacing)
                                        .build();
                        }
                        
                        cameraProvider.unbindAll();
                        if (imageProcessor == null) {
                            cameraProvider.bindToLifecycle(this, cameraSelector, preview);

                            return;
                        }

                    
                        ImageAnalysis imageAnalysis =
                                new ImageAnalysis.Builder()
                                        .setBackpressureStrategy(
                                                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build();

                        imageAnalysis.setAnalyzer(
                                cameraExecutor,
                                new ImageAnalysis.Analyzer() {
                                    @Override
                                    public void analyze(@NonNull ImageProxy imageProxy) {

                                        if (needUpdateGraphicOverlayImageSourceInfo) {
                                            boolean isImageFlipped =
                                                    lensFacing == CameraSelector.LENS_FACING_FRONT;
                                            int rotationDegrees =
                                                    imageProxy.getImageInfo().getRotationDegrees();
                                            if (rotationDegrees == 0 || rotationDegrees == 180) {
                                                graphicOverlay.setImageSourceInfo(
                                                        imageProxy.getWidth(),
                                                        imageProxy.getHeight(),
                                                        isImageFlipped);
                                            } else {
                                                graphicOverlay.setImageSourceInfo(
                                                        imageProxy.getHeight(),
                                                        imageProxy.getWidth(),
                                                        isImageFlipped);
                                            }
                                            needUpdateGraphicOverlayImageSourceInfo = false;
                                        }
                                        try {
                                            imageProcessor.processImageProxy(
                                                    imageProxy, graphicOverlay);
                                            // processImageProxy(imageProxy);
                                        } catch (Exception e) {
                                            Log.e(
                                                    "ml-error",
                                                    "Failed to process image. Error: "
                                                            + e.getLocalizedMessage());
                                            Toast.makeText(
                                                            getApplicationContext(),
                                                            e.getLocalizedMessage(),
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });

                        cameraProvider.bindToLifecycle(
                                this, cameraSelector, preview, imageAnalysis);

                    } catch (ExecutionException | InterruptedException e) {
                        // No errors need to be handled for this Future.
                        // This should never be reached.
                    }
                },
                ContextCompat.getMainExecutor(this));
    }

    private void loadCsvData() {
        // Create a new thread
        AccuratePoseDetectorOptions options =
                new AccuratePoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                        .build();

        new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                // Perform the background task
                                PoseClassifierProcessor pcf =
                                        new PoseClassifierProcessor(
                                                getApplication(),
                                                true,
                                                new String[] {
                                                    workoutType
                                                });
                                // Update the UI on the main thread
                                uiHandler.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                imageProcessor =
                                                        new PoseDetectorProcessor(
                                                                getApplicationContext(),
                                                                options,
                                                                false,
                                                                false,
                                                                false,
                                                                true,
                                                                true,
                                                                pcf, (poseClassification)->{
                                                                    if(poseClassification.size()>=2){ 
                                                                       checkIfWorkoutComplete(poseClassification.get(0));
                                                                    }
                                                                    
                                                                });
                                                startButton.setText(getString(R.string.start));
                                                startButton.setEnabled(true);
                                            }
                                        });
                            }
                        })
                .start();
    }
    
    private void switchCameraLens(){
    if (cameraProvider == null) {
      return;
    }
    int newLensFacing =
        lensFacing == CameraSelector.LENS_FACING_FRONT
            ? CameraSelector.LENS_FACING_BACK
            : CameraSelector.LENS_FACING_FRONT;
    CameraSelector newCameraSelector =
        new CameraSelector.Builder().requireLensFacing(newLensFacing).build();
    try {
      if (cameraProvider.hasCamera(newCameraSelector)) {
        lensFacing = newLensFacing;
        cameraSelector = newCameraSelector;
         startCamera();       
                         
        return;
      }
    } catch (CameraInfoUnavailableException e) {
      // Falls through
    }
    Toast.makeText(
            getApplicationContext(),
            "This device does not have lens with facing: " + newLensFacing,
            Toast.LENGTH_SHORT)
        .show();
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageProcessor != null) {
            imageProcessor.stop();
        }
    }
    

    @Override
    public void onNewRepOccured(List<String> poseClassification) {
      for (int i = 0; i < poseClassification.size(); i++) {
        startButton.setText(poseClassification.get(i));
        }
    }
    
    public int formatReps(String unFormattedRep){
        Matcher matcher = Pattern.compile("\\d+").matcher(unFormattedRep);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }
    
    public void checkIfWorkoutComplete(String unformattedRep){
        int formattedRep = formatReps(unformattedRep);
        if(formattedRep>=reps_final_count){
            DigiUtils.sendNotification(getApplicationContext(),"Quest Complete","You earned 1 Aura point.",R.drawable.swords);
            if (imageProcessor != null) {
                imageProcessor.stop();
            }
            sp.edit().putInt(workoutType,reps_final_count).apply();
            isQuestRunning = true;
            showQuestCompleteDialog();
            CoinManager.incrementCoin(this);
        }
    }
    
    public void showQuestCompleteDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
		.setTitle("Quest Complete")
		.setCancelable(false)
        .setMessage("You earned 1 Aura point")
        
        .setNegativeButton("Quit",(dialog,which)->{
            dialog.dismiss();
            finish();
        });
        builder.create().show();
    }
    
    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            makeCameraPermissionDialog().create().show();
        }
    }
    
    private MaterialAlertDialogBuilder makeCameraPermissionDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.missing_permission)
                    .setMessage(R.string.notification_camera_permission)
                    .setNeutralButton("Provide",(dialog,which)->{
                        requestPermissions(
                        new String[]{Manifest.permission.CAMERA},0);
				  
                    });
        return builder;
    }
}
