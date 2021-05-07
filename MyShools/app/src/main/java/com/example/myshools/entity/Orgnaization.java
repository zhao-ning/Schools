package com.example.myshools.entity;

public class Orgnaization {
    private int id;
    private int schoolId;
    private String name;
    private String level;
    private int popularity;
    private String headPath;
    private String recommend;
    private int identity;
    private String identityContent;
    private String connectionImgPath;
    private String connectionContent;
    private String phone;
    private String password;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
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

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getIdentityContent() {
        return identityContent;
    }

    public void setIdentityContent(String identityContent) {
        this.identityContent = identityContent;
    }

    public String getConnectionImgPath() {
        return connectionImgPath;
    }

    public void setConnectionImgPath(String connectionImgPath) {
        this.connectionImgPath = connectionImgPath;
    }

    public String getConnectionContent() {
        return connectionContent;
    }

    public void setConnectionContent(String connectionContent) {
        this.connectionContent = connectionContent;
    }

    @Override
    public String toString() {
        return "Orgnaization{" +
                "id=" + id +
                ", schoolId=" + schoolId +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", popularity=" + popularity +
                ", headPath='" + headPath + '\'' +
                ", recommend='" + recommend + '\'' +
                ", identity=" + identity +
                ", identityContent='" + identityContent + '\'' +
                ", connectionImgPath='" + connectionImgPath + '\'' +
                ", connectionContent='" + connectionContent + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
