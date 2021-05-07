package com.example.myshools.Mine.organization.historyActivityManage.entity;

/*
 *
 * 组织在活动管理查看报名申请表时所用的类
 *导出excel表
 *
 * */
public class SimpleUserDataNoId {

    private String stuName;
    private String stuNumber;
    private String stuCollege;


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
                ", stuName='" + stuName + '\'' +
                ", stuNumber='" + stuNumber + '\'' +
                ", stuCollege='" + stuCollege + '\'' +
                '}';
    }
}
