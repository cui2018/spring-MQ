package com.properpush.util;

import lombok.Data;

/**
 * @Auther: cui
 * @Date: 2019/1/4 08:59
 * @Description: 返回数据封装类
 */
@Data
public class Result {
    private String status;
    private String msg;
    private Object data;
    private ResultEnum resultEnum;

    public Result(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Result(ResultEnum resultEnum, Object data) {
        this.status = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.data = data;
    }

    public Result(ResultEnum resultEnum) {
        this.status = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }
}
