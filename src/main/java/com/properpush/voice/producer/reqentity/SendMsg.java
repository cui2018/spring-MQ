package com.properpush.voice.producer.reqentity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Auther: cui
 * @Date: 2019/1/2 10:58
 * @Description: 请求参数类
 */
@Data
public class SendMsg {
    private String phone; //通知手机号
    private String numberCode; //Mobile语音通知码号
    private String playTimes; //Mobile播放次数
    private String templateId; //模板id
    private JSONObject obj; //Mobile模板参数
    private String respUrl; //移动语音服务回调地址
    private String code; //验证码
    private String product; //阿里变量
    private String text; //文字描述
    private String type; //推送的类型
    private String produceTime; //发送时间
    private int sendNum; //发送次数
}
