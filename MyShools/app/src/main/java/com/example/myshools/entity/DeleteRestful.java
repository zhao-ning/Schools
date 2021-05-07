package com.example.myshools.entity;

import java.util.Map;

public class DeleteRestful {
    private int code;
    private String msg;
    private int data;
    private Map<String, Object> result;

    public DeleteRestful() {

    }

    public DeleteRestful(Status status) {
        code = status.code;
        msg = status.data;
    }

    public DeleteRestful(Status status, String msg) {
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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}