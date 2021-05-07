package com.example.myshools.entity;

import java.util.Map;

public class Restful1212 {
    private int code;
    private String msg;
    private Object data;
    private Map<String, Object> result;

    public Restful1212() {

    }

    public Restful1212(Status status) {
        code = status.code;
        msg = status.data;
    }

    public Restful1212(Status status, String msg) {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}