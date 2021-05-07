package com.example.myshools.entity;

import java.util.Map;

public class Restful {
    private int code;
    private String msg;
    private Student data;
    private Map<String, Object> result;

    public Restful() {

    }

    public Restful(Status status) {
        code = status.code;
        msg = status.data;
    }

    public Restful(Status status, String msg) {
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

    public Student getData() {
        return data;
    }

    public void setData(Student data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}