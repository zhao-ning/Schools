package com.example.myshools.entity;

import android.app.Activity;

import java.util.Map;

public class RestfulActivity {
    private Integer code;
    private String msg;
    private Activities data;
    private Map<String, Object> result;

    public RestfulActivity() {

    }

    public RestfulActivity(Status status) {
        code = status.code;
        msg = status.data;
    }

    public RestfulActivity(Status status, String msg) {
        code = status.code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Activities getData() {
        return data;
    }

    public void setData(Activities data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}