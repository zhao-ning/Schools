package com.example.myshools.Activity.entity;

import com.example.myshools.Activity.entity.GetActivityResultInfo;

import java.util.Map;

/**
 * @program: d
 * @description: 获取活动一般返回结果类
 * @author: zn
 * @create: 2021-03-23 18:17
 **/
public class GetActivityResult {
    private Integer code;
    private String msg;
    private GetActivityResultInfo data;
    private Map<String,Object> result;


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
    public GetActivityResultInfo getData() {
        return data;
    }
    public void setData(GetActivityResultInfo data) {
        this.data = data;
    }

    public Map<String, Object> getResult() {
        return result;
    }
    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}

