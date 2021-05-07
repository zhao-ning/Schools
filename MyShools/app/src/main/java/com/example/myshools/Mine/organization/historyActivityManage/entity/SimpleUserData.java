package com.example.myshools.Mine.organization.historyActivityManage.entity;

/*
 *
 * 组织在活动管理查看报名申请表时所用的类
 *
 * 报名申请表中，申请人的简单信息
 * id,名字，学院，学号
 *
 * */
public class SimpleUserData {
    private int id;
    private String stuName;
    private String stuNumber;
    private String stuCollege;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getStuCollege() {
        return stuCollege;
    }

    public void setStuCollege(String stuCollege) {
        this.stuCollege = stuCollege;
    }

    @Override
    public String toString() {
        return "SimpleUserData{" +
                "id=" + id +
                ", stuName='" + stuName + '\'' +
                ", stuNumber='" + stuNumber + '\'' +
                ", stuCollege='" + stuCollege + '\'' +
                '}';
    }
}
