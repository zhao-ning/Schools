package com.example.myshools.Activity.entity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/*
赵宁：listview嵌套listview
* listview和gridview都是可滑动的控件（Scrollbar）
* 在Android中，不能在一个拥有Scrollbar的组件中嵌入另一个拥有Scrollbar的组件
* 因为这不科学，会混淆滑动事件，导致只显示一到两行数据。
* 那么就换一种思路，首先让子控件的内容全部显示出来，禁用了它的滚动。
* 如果超过了父控件的范围则显示父控件的scrollbar滚动显示内容，具体的方法是：
* 自定义Listview组件，继承自listview。重载onMeasure方法：
*
*
* */
public class NoScrollListView extends ListView {
    public NoScrollListView(Context context) {
        super(context);
    }
    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
