package com.bigcat.laundry.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 响应码 1=成功，0=失败
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
     * 成功结果
     *
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功结果
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(1);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败结果
     *
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> error() {
        return error("操作失败");
    }

    /**
     * 失败结果
     *
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }
} 