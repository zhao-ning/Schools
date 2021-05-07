package com.example.myshools.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForBidEmojiEditText extends androidx.appcompat.widget.AppCompatEditText {
    public ForBidEmojiEditText(Context context) {
        super(context);
    }

    public ForBidEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ForBidEmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void setFilters(InputFilter[] filters) {
        InputFilter emojiFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
                Pattern emoji = Pattern.compile(
                        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    return "";
                }
                return null;
            }
        };
        super.setFilters(new InputFilter[]{emojiFilter});
    }

}
