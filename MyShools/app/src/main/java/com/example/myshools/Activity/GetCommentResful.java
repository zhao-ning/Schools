package com.example.myshools.Activity;

import com.example.myshools.entity.Status;
import com.example.myshools.entity.Student;

import java.util.List;
import java.util.Map;

public class GetCommentResful {
    private int code;
    private String msg;
    private List<ResultComment> data;
    private Map<String, Object> result;

    public GetCommentResful() {

    }

    public GetCommentResful(Status status) {
        code = status.code;
        msg = status.data;
    }

    public GetCommentResful(Status status, String msg) {
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

    public List<ResultComment> getData() {
        return data;
    }

    public void setData(List<ResultComment> data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}