package com.example.myshools.Mine.organization.historyActivityManage.entity;
/*
*
* 组织在活动管理查看报名申请表时所用的类
*
* 报名申请表中，申请信息,用户简单信息
*
* */
public class ApplyForm {
    private int id;
    private int aId;
    private int userId;
    private int applyResult;
    private String questionAnswer;
    private String applyTime;
    private SimpleUserData userData;

    public SimpleUserData getUserData() {
        return userData;
    }

    public void setUserData(SimpleUserData userData) {
        this.userData = userData;
    }

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getApplyResult() {
        return applyResult;
    }

    public void setApplyResult(int applyResult) {
        this.applyResult = applyResult;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    @Override
    public String toString() {
        return "ApplyForm{" +
                "id=" + id +
                ", aId=" + aId +
                ", userId=" + userId +
                ", applyResult=" + applyResult +
                ", questionAnswer='" + questionAnswer + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", userData=" + userData +
                '}';
    }
}
