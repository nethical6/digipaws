package nethical.digipaws.view;

import android.view.View;
import android.widget.TextView;
import android.widget.ProgressBar;
import nethical.digipaws.R;
import android.view.Window;
import android.content.Context;
import android.app.Dialog;

public class CustomLoadingDialog {

  private Dialog dialog;
  private TextView messageText;
  private ProgressBar progressBar;

  public CustomLoadingDialog(Context context) {
    dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove title bar
    dialog.setContentView(R.layout.loading_dialog); // Set your custom layout
    dialog.setCanceledOnTouchOutside(false);

    // Find the progress bar and text view in your layout (if any)

    ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
    TextView messageText = dialog.findViewById(R.id.message_text);
    dialog.show();
  }

  public void setTitle(String message) {}

  public void dismissDialog() {
    if (dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  // (Optional) Allow setting progress bar style (e.g., determinate/indeterminate)

}
