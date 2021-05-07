package com.example.myshools.entity;

import java.util.Map;

public class Restful2 {
    private Integer code;
    private String msg;
    private Orgnaization data;
    private Map<String, Object> result;

    public Restful2() {

    }

    public Restful2(Status status) {
        code = status.code;
        msg = status.data;
    }

    public Restful2(Status status, String msg) {
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

    public Orgnaization getData() {
        return data;
    }

    public void setData(Orgnaization data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}