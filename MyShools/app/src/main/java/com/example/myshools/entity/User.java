package com.example.myshools.entity;

import java.time.LocalDateTime;

public class User {
    private int id;

    public User(String name,String headImgUrl){
        this.name=name;
        this.headImgUrl=headImgUrl;
    }
    /**
     * 用户状态:0=正常,1=禁用
     */
    private Boolean state;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像图片地址
     */
    private String headImgUrl;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 密码加盐
     */
    private String salt;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改时间
     */
    private LocalDateTime edited;

    /**
     * 修改人
     */
    private String editor;

    /**
     * 逻辑删除:0=未删除,1=已删除
     */
    private Boolean deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getEdited() {
        return edited;
    }

    public void setEdited(LocalDateTime edited) {
        this.edited = edited;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
