package com.bigcat.laundry.common.result;

import lombok.Data;

/**
 * 统一响应结果模型
 *
 * @param <T> 响应数据类型
 */
@Data
public class ResultModel<T> {

    /**
     * 响应码（1-成功，0-失败，-1-登录失效）
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 成功响应
     *
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResultModel<T> ok(T data) {
        ResultModel<T> result = new ResultModel<>();
        result.setCode(1);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResultModel<T> ok() {
        return ok(null);
    }

    /**
     * 失败响应
     *
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResultModel<T> error(String msg) {
        ResultModel<T> result = new ResultModel<>();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }

    /**
     * 失败响应
     *
     * @param code 错误码
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResultModel<T> error(Integer code, String msg) {
        ResultModel<T> result = new ResultModel<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 登录失效响应
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResultModel<T> unauthorized() {
        ResultModel<T> result = new ResultModel<>();
        result.setCode(-1);
        result.setMsg("登录已失效，请重新登录");
        return result;
    }
} 