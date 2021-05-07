package com.example.myshools.entity;

public class ApplyManage {
    private int id;
    private int aId;

    private int uId;

    private int applyResult;

    private String questionanswer;

    private String applyTime;

    private Activities activities;
    private Student student;

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

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getApplyResult() {
        return applyResult;
    }

    public void setApplyResult(int applyResult) {
        this.applyResult = applyResult;
    }

    public String getQuestionanswer() {
        return questionanswer;
    }

    public void setQuestionanswer(String questionanswer) {
        this.questionanswer = questionanswer;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public Activities getActivities() {
        return activities;
    }

    public void setActivities(Activities activities) {
        this.activities = activities;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
