package com.example.myshools.entity;

import java.util.List;
import java.util.Map;

public class GetApplyRestful {
    private int code;
    private String msg;
    private List<ApplyManage> data;
    private Map<String, Object> result;

    public GetApplyRestful() {

    }

    public GetApplyRestful(Status status) {
        code = status.code;
        msg = status.data;
    }

    public GetApplyRestful(Status status, String msg) {
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

    public List<ApplyManage> getData() {
        return data;
    }

    public void setData(List<ApplyManage> data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}