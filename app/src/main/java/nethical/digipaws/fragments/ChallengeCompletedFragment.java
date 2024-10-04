package nethical.digipaws.fragments;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import androidx.core.content.FileProvider;
import com.google.android.datatransport.BuildConfig;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.net.Uri;
import com.google.android.material.internal.EdgeToEdgeUtils;
import java.io.File;
import android.os.Environment;
import java.io.FileOutputStream;
import java.io.IOException;
import nethical.digipaws.MainActivity;
import nethical.digipaws.fragments.dialogs.SelectQuestDialog;
import nethical.digipaws.fragments.intro.ConfigureWarning;
import nethical.digipaws.fragments.quests.FocusQuest;
import nethical.digipaws.receivers.AdminReceiver;
import android.content.Context;
import java.util.Date;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.TimeUnit;
import nethical.digipaws.R;
import nethical.digipaws.fragments.dialogs.LoadingDialog;
import nethical.digipaws.services.BlockerService;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.LoadAppList;

import java.util.List;
import nethical.digipaws.views.HollowCircleView;

public class ChallengeCompletedFragment extends Fragment {

    private View view;
    
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