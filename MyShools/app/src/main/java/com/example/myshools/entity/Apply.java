package com.example.myshools.entity;

public class Apply {
    private int id;

    private int aId;

    private int uId;

    private int applyResult;

    private String questionanswer;

    private String applyTime;

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

    @Override
    public String toString() {
        return "Apply{" +
                "id=" + id +
                ", aId=" + aId +
                ", uId=" + uId +
                ", applyResult=" + applyResult +
                ", questionanswer='" + questionanswer + '\'' +
                ", applyTime='" + applyTime + '\'' +
                '}';
    }
}
