package org.arendelle.android;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeHighlighter {

    // pattern
    private Pattern singleLineComments;
    //private Pattern multiLineComments;
    private Pattern dataTypes;
    private Pattern numbers;
    private Pattern brackets;


    public CodeHighlighter() {

        // compile pattern
        singleLineComments = Pattern.compile("(--|\\/\\/).*");
        //multiLineComments = Pattern.compile("");
        dataTypes = Pattern.compile("[\\$\\&\\#\\@\\!][a-zA-Z0-9\\.\\_ ]*\\??");
        numbers = Pattern.compile("[0-9]+(\\.[0-9]+)?");
        brackets = Pattern.compile("[\\(\\)\\[\\]\\{\\}\\<\\>\\,]");

    }

    /** applies code highlighting */
    public void highlight(Context context, EditText editText) {

        String text = editText.getText().toString();
        Editable editable = editText.getEditableText();
        highlightSingle(editable, text, numbers, context.getResources().getColor(R.color.colorNumbers));
        highlightSingle(editable, text, dataTypes, context.getResources().getColor(R.color.colorDataTypes));
        highlightSingle(editable, text, brackets, context.getResources().getColor(R.color.colorBrackets));
        highlightSingle(editable, text, singleLineComments, context.getResources().getColor(R.color.colorComments));
        //highlightSingle(editable, text, multiLineComments, context.getResources().getColor(R.color.colorComments));

    }

    private void highlightSingle(Editable editable, String text, Pattern pattern, int color) {

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            editable.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

}
