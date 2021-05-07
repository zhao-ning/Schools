package com.example.myshools.Activity;

import com.example.myshools.entity.Comment;

import java.util.ArrayList;
import java.util.List;

import com.example.myshools.entity.User;

public class ResultComment {
    private Integer id;
    private List<ReplyComment> children;
    private User user;
    private Integer newsId;
    private String content;
    private String createdTime;
    private Integer status;

    public ResultComment(Comment comment) {
        this.id = comment.getId();
        this.newsId = comment.getNewsId();
        this.content = comment.getContent();
        this.createdTime = comment.getCreateTime();
        this.status = comment.getStatus();
        this.children = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChildren(List<ReplyComment> children) {
        this.children = children;
    }

    public List<ReplyComment> getChildren() {
        return children;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResultComment{" +
                "id=" + id +
                ", children=" + children +
                ", user=" + user +
                ", newsId=" + newsId +
                ", content='" + content + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", status=" + status +
                '}';
    }
}


