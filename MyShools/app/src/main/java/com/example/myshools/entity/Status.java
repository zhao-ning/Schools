package com.example.myshools.entity;

/**
 * @program: demo
 * @description: 状态类
 * @author: Charlotte
 * @create: 2021-02-26 18:49
 **/
public enum Status {


    // 公共
    SUCCESS(2000, "成功"),
    UNKNOWN_ERROR(9998,"未知异常"),
    SYSTEM_ERROR(9999, "系统异常"),


    INSUFFICIENT_PERMISSION(4003, "权限不足"),

    WARN(9000, "失败"),
    REQUEST_PARAMETER_ERROR(1002, "请求参数错误"),

    // 登录
    LOGIN_EXPIRE(2001, "未登录或者登录失效"),
    LOGIN_CODE_ERROR(2002, "登录验证码错误"),
    LOGIN_ERROR(2003, "用户名不存在或密码错误"),
    LOGIN_USER_STATUS_ERROR(2004, "用户状态不正确"),
    LOGOUT_ERROR(2005, "退出失败，token不存在"),
    LOGIN_USER_NOT_EXIST(2006, "该用户不存在"),
    LOGIN_USER_EXIST(2007, "该用户已存在"),

    //注册
    REGISTER_USER_EXIST(3007, "该用户名已存在"),
    REGISTER_USER_FAILE(3008, "该用户名格式不正确");
    public int code;
    public String data;
    Status(int code, String message) {
        this.code = code;
        this.data = message;
    }

}
