package com.example.myshools.Activity.entity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ScrollView;
/*
* 滑动距离监听
* */

public class MySrollView extends ScrollView {
    private OnScrollListener listener;

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }
    // 滑动距离监听器
    public interface OnScrollListener{
        /**
         * 在滑动的时候调用，scrollY为已滑动的距离
         */
        void onScroll(int scrollY);
    }

    public MySrollView(Context context) {
        super(context);
    }

    public MySrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(listener!=null){
            listener.onScroll(getScrollY());
        }
    }
}
