package com.example.myshools.entity;

public class PeopleNumManagement {
    private int id;
    private int aId;//活动id
    private int total;//总人数
    private int joinNum;//参加的人数
    private int freeNum;//剩余的空位
    private int applyingNum;//正在申请的人数
    public PeopleNumManagement(int aId,int total,int joinNum,int freeNum,int applyingNum){
        this.aId=aId;
        this.total=total;
        this.joinNum=joinNum;
        this.freeNum=freeNum;
        this.applyingNum=applyingNum;
    }
    public PeopleNumManagement(){}
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
        return "PeopleNumManagement{" +
                "id=" + id +
                ", aId=" + aId +
                ", total=" + total +
                ", joinNum=" + joinNum +
                ", freeNum=" + freeNum +
                ", applyingNum=" + applyingNum +
                '}';
    }
}
