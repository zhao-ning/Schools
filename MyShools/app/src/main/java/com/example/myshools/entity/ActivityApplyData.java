package com.example.myshools.entity;

public class ActivityApplyData {
    private int id;
    private int aId;
    private int total;
    private int joinNum;
    private int freeNum;
    private int applyingNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public int getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(int freeNum) {
        this.freeNum = freeNum;
    }

    public int getApplyingNum() {
        return applyingNum;
    }

    public void setApplyingNum(int applyingNum) {
        this.applyingNum = applyingNum;
    }

    @Override
    public String toString() {
        return "ActivityApplyData{" +
                "id=" + id +
                ", aId=" + aId +
                ", total=" + total +
                ", joinNum=" + joinNum +
                ", freeNum=" + freeNum +
                ", applyingNum=" + applyingNum +
                '}';
    }
}
