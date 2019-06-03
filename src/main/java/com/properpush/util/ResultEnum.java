package com.properpush.util;

import lombok.Getter;

/**
 * @Auther: cui
 * @Date: 2019/1/4 10:14
 * @Description:
 */
@Getter
public enum ResultEnum {
    SUCCESS("200", "SUCCESS"),
    ERROR("400", "ERROR"),
    PARMERROR("401", "parm-error"),
    MOULDERROR("402","mould-parm-error"),
    CONFPARMERROR("403", "conf-parm-error"),
    SIGNERROR("405","sign-error"),
    UNKNOWNERROR("406","unknown-error");

    private String code;
    private String msg;

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
