package com.example.myshools.entity;

public class Activities{
    private int id;

    private int origanizationId;

    private String upTime;

    private Integer viewNum;

    private String content;

    private String label;

    private String title;

    private int status;

    private int isSetJoinNum;


    private Orgnaization organization;//组织在查询之后重新添加
    private PeopleNumManagement peopleNumManagement;//活动参加信息


    /**
     * 活动图片
     */
    private String imgs;

    /**
     * 备用字段
     */

    private String questions;

    /**
     * 备用字段
     */
    private int userApplyStatus;////用户是否申请参加活动，若参加活动返回的结果，参加成功还是失败
    private int toApply;//用户是否正在提交申请

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOriganizationId() {
        return origanizationId;
    }

    public void setOriganizationId(Integer origanizationId) {
        this.origanizationId = origanizationId;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsSetJoinNum() {
        return isSetJoinNum;
    }

    public void setIsSetJoinNum(int isSetJoinNum) {
        this.isSetJoinNum = isSetJoinNum;
    }

    public Orgnaization getOrganization() {
        return organization;
    }

    public void setOrganization(Orgnaization organization) {
        this.organization = organization;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getquestions() {
        return questions;
    }

    public void setquestions(String questions) {
        this.questions = questions;
    }

    public int getUserApplyStatus() {
        return userApplyStatus;
    }

    public void setUserApplyStatus(int userApplyStatus) {
        this.userApplyStatus = userApplyStatus;
    }

    public int getToApply() {
        return toApply;
    }

    public void setToApply(int toApply) {
        this.toApply = toApply;
    }

    public PeopleNumManagement getPeopleNumManagement() {
        return peopleNumManagement;
    }

    public void setPeopleNumManagement(PeopleNumManagement peopleNumManagement) {
        this.peopleNumManagement = peopleNumManagement;

    }

    @Override
    public String toString() {
        return "Activities{" +
                "id=" + id +
                ", origanizationId=" + origanizationId +
                ", upTime='" + upTime + '\'' +
                ", viewNum=" + viewNum +
                ", content='" + content + '\'' +
                ", label='" + label + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", isSetJoinNum=" + isSetJoinNum +
                ", organization=" + organization +
                ", peopleNumManagement=" + peopleNumManagement +
                ", imgs='" + imgs + '\'' +
                ", questions='" + questions + '\'' +
                ", userApplyStatus=" + userApplyStatus +
                ", toApply=" + toApply +
                '}';
    }
}
