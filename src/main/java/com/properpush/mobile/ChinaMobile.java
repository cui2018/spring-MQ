package com.properpush.mobile;

import com.alibaba.fastjson.JSONObject;
import com.properpush.mobile.Resp.MobileResp;
import com.properpush.util.HttpClient;
import com.properpush.util.MD5Util;
import com.properpush.util.PropertyPlaceholder;
import com.properpush.voice.producer.reqentity.SendMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cui
 * @Date: 2019/1/8 11:08
 * @Description:
 */
@Slf4j
public class ChinaMobile {
    /**
     * 文本转语音外呼
     *
     * @return
     * @throws
     */
    public static MobileResp sendMobile(SendMsg msg) {
        String apiKey = PropertyPlaceholder.getProperty("Mobile.apiKey");
        String timeStr = String.valueOf(System.currentTimeMillis());
        String signStr = MD5Util.getMd5(apiKey + PropertyPlaceholder.getProperty("Mobile.secretKey") + timeStr);
        Map signMap = new HashMap(3);
        signMap.put("apiKey", apiKey);
        signMap.put("time", timeStr);
        signMap.put("sign", signStr);
        Map map = new HashMap(1);
        map.put("Authorization", Base64.encodeBase64String(JSONObject.toJSONString(signMap).getBytes()));
        ResponseEntity<byte[]> entity;
        String reqParm = getParm(msg);
        MobileResp mobileResp = null;
        try {
            log.info("请求移动语音参数：{}", reqParm);
            entity = HttpClient.post(PropertyPlaceholder.getProperty("Mobile.URL"), map, MediaType.APPLICATION_JSON_UTF8, reqParm);
            byte[] httpRespBody = entity.getBody();
            String str = new String(httpRespBody, "UTF-8");
            mobileResp = JSONObject.parseObject(str, MobileResp.class);
            log.info("移动语音服务回调：{}", mobileResp);
        } catch (Exception e) {
            log.warn("移动语音服务发送异常，异常信息：{}", e);
        }
        return mobileResp;
    }

    private static String getParm(SendMsg msg) {
        JSONObject obj = new JSONObject();
        obj.put("voiceNumber", msg.getNumberCode());
        obj.put("to", msg.getPhone());
        obj.put("playTimes", msg.getPlayTimes());
        obj.put("templateId", msg.getTemplateId());
        if (msg.getObj() != null) {
            obj.put("templateParameter", msg.getObj());
        }
        if (StringUtils.isNotBlank(msg.getRespUrl())) {
            obj.put("respUrl", msg.getRespUrl());
        }
        return JSONObject.toJSONString(obj);
    }

}
