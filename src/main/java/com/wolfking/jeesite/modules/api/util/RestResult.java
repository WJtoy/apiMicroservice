package com.wolfking.jeesite.modules.api.util;

public class RestResult<T> {
    private Integer code;    //返回码
    private String msg;        //返回提示信息
    private T data;    //返回数据

    private RestResult() {
    }

    public static <T> RestResult<T> newInstance() {
        return new RestResult<>();
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
