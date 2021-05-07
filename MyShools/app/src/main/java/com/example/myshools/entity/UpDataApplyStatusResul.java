package com.example.myshools.entity;

import com.example.myshools.Mine.organization.historyActivityManage.entity.SetUpResult;

import java.util.List;
import java.util.Map;

public class UpDataApplyStatusResul {
    private int code;
    private String msg;
    private List<SetUpResult> data;
    private Map<String, Object> result;
    public UpDataApplyStatusResul() {
    }
    public UpDataApplyStatusResul(Status status) {
        code = status.code;
        msg = status.data;
    }
    public UpDataApplyStatusResul(Status status, String msg) {
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
    public List<SetUpResult> getData() {
        return data;
    }
    public void setData(List<SetUpResult> data) {
        this.data = data;
    }
    public Map<String, Object> getResult() {
        return result;
    }
    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}