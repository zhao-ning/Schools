package com.example.myshools.entity;

public class EventMessage {
    private int position;
    private int applyStatus;
    public EventMessage(int position,int applyStatus){
        this.applyStatus=applyStatus;
        this.position=position;
    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(int applyStatus) {
        this.applyStatus = applyStatus;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "position=" + position +
                ", applyStatus=" + applyStatus +
                '}';
    }
}
