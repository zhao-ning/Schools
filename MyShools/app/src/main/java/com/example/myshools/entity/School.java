package com.example.myshools.entity;

public class School {
    private Integer id=-1;

    private String schoolName;

    private String schoolImgpath;

    private String schoolCity;

    private String schoolCollegeList;

    private String schoolNewsType;

    private String schoolProvince;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolImgpath() {
        return schoolImgpath;
    }

    public void setSchoolImgpath(String schoolImgpath) {
        this.schoolImgpath = schoolImgpath;
    }

    public String getSchoolCity() {
        return schoolCity;
    }

    public void setSchoolCity(String schoolCity) {
        this.schoolCity = schoolCity;
    }

    public String getSchoolCollegeList() {
        return schoolCollegeList;
    }

    public void setSchoolCollegeList(String schoolCollegeList) {
        this.schoolCollegeList = schoolCollegeList;
    }

    public String getSchoolNewsType() {
        return schoolNewsType;
    }

    public void setSchoolNewsType(String schoolNewsType) {
        this.schoolNewsType = schoolNewsType;
    }

    public String getSchoolProvince() {
        return schoolProvince;
    }

    public void setSchoolProvince(String schoolProvince) {
        this.schoolProvince = schoolProvince;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", schoolName='" + schoolName + '\'' +
                ", schoolImgpath='" + schoolImgpath + '\'' +
                ", schoolCity='" + schoolCity + '\'' +
                ", schoolCollegeList='" + schoolCollegeList + '\'' +
                ", schoolNewsType='" + schoolNewsType + '\'' +
                ", schoolProvince='" + schoolProvince + '\'' +
                '}';
    }
}
