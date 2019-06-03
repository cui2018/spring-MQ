package com.properpush.mobile.Resp;

import lombok.Data;

/**
 * @Auther: cui
 * @Date: 2019/1/8 15:07
 * @Description: 移动语音服务返回信息
 */
@Data
public class MobileResp {
    private int resultCode;
    private String resultMsg;
    private String callId; //服务端通话的唯一标识
}
