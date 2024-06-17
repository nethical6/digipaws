package nethical.digipaws.fragments.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.Spanned;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import nethical.digipaws.R;
import nethical.digipaws.utils.DigiConstants;

public class TermsAndConditions extends Fragment {

    private TextView text;
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.accept_toc, container, false);
        text = view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SpannableString st = new SpannableString(getContext().getString(R.string.tac));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Open TOC URL in a new activity (or fragment)
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DigiConstants.WEBSITE_ROOT+"terms-and-conditions"));
                startActivity(intent);
            }
        };
        int startIdx = 32;
        int endIdx = 52;
        st.setSpan(clickableSpan, startIdx, endIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(st);
        text.setMovementMethod(LinkMovementMethod.getInstance()); // Enable link movement

    }
}
