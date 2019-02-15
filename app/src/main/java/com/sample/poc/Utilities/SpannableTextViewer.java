package com.sample.poc.Utilities;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.sample.poc.R;

/**
 * Created by 1013373 on 8/8/2018.
 */

public class SpannableTextViewer {


    public static void displaySpannableText(String text, TextView textView)
    {

        final SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textView.setText(content);
    }
}
