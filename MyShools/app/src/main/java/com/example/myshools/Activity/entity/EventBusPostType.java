package com.example.myshools.Activity.entity;

import com.example.myshools.entity.School;

public class EventBusPostType {
    private int type;
    private School school;
    private int position;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
