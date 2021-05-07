package com.example.myshools.Activity.entity;

public class SelectNewsType {
    private String type;
    private boolean isSelected=false;
    public SelectNewsType(String ty,boolean is){
        this.type=ty;
        this.isSelected=is;
    }
    public SelectNewsType(){}
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
