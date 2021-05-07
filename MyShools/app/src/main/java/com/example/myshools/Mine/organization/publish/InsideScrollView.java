package com.example.myshools.Mine.organization.publish;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class InsideScrollView extends ScrollView {
    public InsideScrollView(Context context) {
        this(context, null);
    }

    public InsideScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //如果我不允许外部拦截我呢？
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

}