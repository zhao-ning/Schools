package com.example.myshools.Mine.organization.publish;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class OutsideScrollView extends ScrollView {

    public OutsideScrollView(Context context) {
        this(context, null);
    }

    public OutsideScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutsideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}