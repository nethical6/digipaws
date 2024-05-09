package nethical.digipaws.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.recyclerview.widget.RecyclerView;

public class KeyboardUtils {
    // Method to show the soft keyboard
    public static void showKeyboard(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            view.requestFocus(); // Set focus to the view
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // Method to hide the soft keyboard if it is currently visible
    public static void hideKeyboard(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Attach a scroll listener to the RecyclerView to hide keyboard on scroll
    public static void attachKeyboardHidingListener(
            final Context context, RecyclerView recyclerView) {
        if (context == null || recyclerView == null) {
            return;
        }

        recyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        // Check if the user initiated a scroll gesture (ACTION_DOWN followed by
                        // ACTION_MOVE)
                        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            View focusedView = recyclerView.findFocus();
                            if (focusedView != null) {
                                hideKeyboard(context, focusedView);
                            }
                        }
                    }
                });
    }
}
