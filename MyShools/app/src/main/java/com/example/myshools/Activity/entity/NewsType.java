package com.example.myshools.Activity.entity;

import androidx.annotation.NonNull;

public class NewsType {
    private String Type;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "NewsType{" +
                "Type='" + Type + '\'' +
                '}';
    }
}
