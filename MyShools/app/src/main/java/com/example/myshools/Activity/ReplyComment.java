package com.example.myshools.Activity;

import com.example.myshools.entity.Comment;

import com.example.myshools.entity.User;

public class ReplyComment {

        private Integer id;
        private Integer parentId;
        private User user;
        private Integer newsId;
        private String content;
        private String createdTime;
        private Integer status;

        public ReplyComment(Comment c) {
            this.id = c.getId();
            this.parentId = c.getParentId();
            this.newsId = c.getNewsId();
            this.content = c.getContent();
            this.createdTime = c.getCreateTime();
            this.status = c.getStatus();
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getParentId() {
            return parentId;
        }

        public void setParentId(Integer parentId) {
            this.parentId = parentId;
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
        return "ReplyComment{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", user=" + user +
                ", newsId=" + newsId +
                ", content='" + content + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", status=" + status +
                '}';
    }
}

