package com.example.myshools.entity;

import java.util.List;
import java.util.Map;

public class StudentGetActivitiesRestful {
    private Integer code;
    private String msg;
    private List<GetStudentApplyActivitiies> data;
    private Map<String,Object> result;

    public StudentGetActivitiesRestful() {
    }
    public StudentGetActivitiesRestful(Status status) {
        code = status.code;
        msg = status.data;
    }

    public StudentGetActivitiesRestful(Status status, String msg) {
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

    public List<GetStudentApplyActivitiies> getData() {
        return data;
    }

    public void setDat(List<GetStudentApplyActivitiies> data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String,Object> result) {
        this.result = result;
    }
}