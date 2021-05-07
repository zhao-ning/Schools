package com.example.myshools.entity;

/*
* 创建活动时点击活动标签返回的结果
* adapter传递给创建活动页面
*
* */
public class ClickEventMessage {

    private int position;
    private boolean is;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isIs() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }
}
