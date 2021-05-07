package com.example.myshools.entity;

import java.util.List;

public class ResultStatus {
    private String code;
    private List<School> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<School> getMessage() {
        return data;
    }

    public void setMessage(List<School> message) {
        this.data = message;
    }

    public  ResultStatus(String code,List<School> data){
        this.code=code;
        this.data=data;
    }
    @Override
    public String toString() {
        return "ResultStatus{" +
                "code=" + code +
                ",data='" + data + '\'' +
                '}';
    }
}
