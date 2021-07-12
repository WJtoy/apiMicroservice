package com.wolfking.jeesite.modules.api.util;

public class RestResultGenerator {


    /**
     * 成功请求
     *
     * @param data
     * @return
     */
    public static <T> RestResult<T> success(Object data) {
        RestResult res = RestResult.newInstance();
        res.setCode(ErrorCode.NO_ERROR.code);
        res.setMsg(ErrorCode.NO_ERROR.message);
        res.setData(data);
        return res;
        //return general(res);
    }

    public static <T> RestResult<T> success() {
        RestResult res = RestResult.newInstance();
        res.setCode(ErrorCode.NO_ERROR.code);
        res.setMsg(ErrorCode.NO_ERROR.message);
        return res;
    }

    public static <T> RestResult<T> success(String msg) {
        RestResult res = RestResult.newInstance();
        res.setCode(ErrorCode.NO_ERROR.code);
        res.setMsg(msg);
        return res;
    }

    /**
     * 请求抛出异常
     *
     * @param msg
     * @return
     */
    public static <T> RestResult<T> exception(String msg) {
        RestResult res = RestResult.newInstance();
        res.setCode(ErrorCode.UNKNOWN_EXCEPTION.code);
        res.setMsg(msg);
        return res;
    }


    public static <T> RestResult<T> unKonwException() {
        RestResult res = RestResult.newInstance();
        res.setCode(ErrorCode.UNKNOWN_EXCEPTION.code);
        res.setMsg(ErrorCode.UNKNOWN_EXCEPTION.message);
        return res;
    }

    /**
     * 请求参数不合法
     */
    public static <T> RestResult<T> requestParameterError() {
        RestResult res = RestResult.newInstance();
        res.setCode(ErrorCode.WRONG_REQUEST_FORMAT.code);
        res.setMsg(ErrorCode.WRONG_REQUEST_FORMAT.message);
        return res;
    }

    /**
     * 请求参数不合法
     */
    public static <T> RestResult<T> requestParameterError(String msg) {
        RestResult res = RestResult.newInstance();
        res.setCode(ErrorCode.WRONG_REQUEST_FORMAT.code);
        res.setMsg(msg);
        return res;
    }

    /**
     * 自定义
     *
     * @param code
     * @param msg
     * @return
     */
    public static <T> RestResult<T> custom(Integer code, String msg) {
        RestResult res = RestResult.newInstance();
        res.setCode(code);
        res.setMsg(msg);
        return res;
    }

    /**
     * 自定义
     *
     * @param code
     * @param msg
     * @return
     */
    public static <T> RestResult<T> custom(Integer code, String msg, Object data) {
        RestResult res = RestResult.newInstance();
        res.setCode(code);
        res.setMsg(msg);
        res.setData(data);
        return res;
    }

}
