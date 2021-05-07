package com.example.myshools.Mine.organization.historyActivityManage.entity;

import java.util.List;

/*
* 一键处理所有申请的返回结果
*
* */
public class SetUpResult {
    private int applyId;
    private int setUpResult;//0 是失败，1是成功，2是申请撤销

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public int getSetUpResult() {
        return setUpResult;
    }

    public void setSetUpResult(int setUpResult) {
        this.setUpResult = setUpResult;
    }

    @Override
    public String toString() {
        return "SetUpResult{" +
                "applyId=" + applyId +
                ", setUpResult=" + setUpResult +
                '}';
    }
}
