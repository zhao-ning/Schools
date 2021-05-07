package com.example.myshools.entity;

public class Student {
    private int id;
    private String phone;
    private String password;
    private Integer schoolId;

    private String stuNumber;

    private String stuName;

    private String stuNickname;

    private String stuCollege;

    private String headPath;

    private String recommend;

    private String identityimgObverse;

    private String identityimgReverse;

    private int isIdentity;

    private String sex;

    private String grade;

    private String specialty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuNickname() {
        return stuNickname;
    }

    public void setStuNickname(String stuNickname) {
        this.stuNickname = stuNickname;
    }

    public String getStuCollege() {
        return stuCollege;
    }

    public void setStuCollege(String stuCollege) {
        this.stuCollege = stuCollege;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getIdentityimgObverse() {
        return identityimgObverse;
    }

    public void setIdentityimgObverse(String identityimgObverse) {
        this.identityimgObverse = identityimgObverse;
    }

    public String getIdentityimgReverse() {
        return identityimgReverse;
    }

    public void setIdentityimgReverse(String identityimgReverse) {
        this.identityimgReverse = identityimgReverse;
    }

    public int getIsIdentity() {
        return isIdentity;
    }

    public void setIsIdentity(int isIdentity) {
        this.isIdentity = isIdentity;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", schoolId=" + schoolId +
                ", stuNumber='" + stuNumber + '\'' +
                ", stuName='" + stuName + '\'' +
                ", stuNickname='" + stuNickname + '\'' +
                ", stuCollege='" + stuCollege + '\'' +
                ", headPath='" + headPath + '\'' +
                ", recommend='" + recommend + '\'' +
                ", identityimgObverse='" + identityimgObverse + '\'' +
                ", identityimgReverse='" + identityimgReverse + '\'' +
                ", isIdentity=" + isIdentity +
                ", sex='" + sex + '\'' +
                ", grade='" + grade + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}
