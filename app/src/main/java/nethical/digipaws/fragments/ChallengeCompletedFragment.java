package nethical.digipaws.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Button;
import androidx.core.content.FileProvider;
import android.net.Uri;
import com.google.android.material.internal.EdgeToEdgeUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.fragment.app.Fragment;


import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;


public class ChallengeCompletedFragment extends Fragment {

    private View view;
    private boolean isChallengeCompleted = false;
    private String day= "0";
    
    public ChallengeCompletedFragment(boolean isCompleted,String day){
        isChallengeCompleted = isCompleted;
        this.day = day;
    }
    public ChallengeCompletedFragment(){  }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        EdgeToEdgeUtils.applyEdgeToEdge(getActivity().getWindow(),false);
        view = inflater.inflate(R.layout.challenge_completed_fragment, container, false);
		return view;
	}
	
    
    
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Button shareBtn = view.findViewById(R.id.share);
        shareBtn.setOnClickListener(v->{
            Bitmap screenshot = takeScreenshot(view);
            Uri screenshotUri = saveBitmapToCache(requireContext(), screenshot);
            shareScreenshot(requireContext(), screenshotUri);
        });
        
        TextView msg = view.findViewById(R.id.text);
        if(isChallengeCompleted){
            msg.setText(getString(R.string.congratulate_text_challenge_comp));
        }
        msg.append("\n\n Stats:");
        SharedPreferences questPref = requireContext().getSharedPreferences(DigiConstants.PREF_QUEST_INFO_FILE, Context.MODE_PRIVATE);
        msg.append("\n💪 Total Pushups: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_PUSHUPS,DigiConstants.DEFAULT_REPS_PUSHUPS)) + " reps");
        msg.append("\n🏋️ Total Squats: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_SQUATS,DigiConstants.DEFAULT_REPS_SQUATS)) + " reps");
        msg.append("\n🏃 Total Distance Ran: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_DISTANCE_RUN,DigiConstants.DEFAULT_METERS_MARATHON)) + " metres");
        msg.append("\n⏲️ Total Time Focused: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_FOCUSED,DigiConstants.DEFAULT_FOCUSED)) + " minutes");
        msg.append("\n📅 Days Elapsed: " + day);
        
	}
    
	
    public Bitmap takeScreenshot(View view) {
        // Create a bitmap with the same dimensions as the view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // Draw the view onto the bitmap
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
}
    public Uri saveBitmapToCache(Context context, Bitmap bitmap) {
        File cachePath = new File(context.getCacheDir(), "images");
        if (!cachePath.exists()) {
            cachePath.mkdirs();
        }
        File file = new File(cachePath, "screenshot.png");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);

}
    public void shareScreenshot(Context context, Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share Screenshot"));
}
  
	
}