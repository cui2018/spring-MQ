package com.properpush.ali.api;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.properpush.util.PropertyPlaceholder;
import com.properpush.voice.producer.reqentity.SendMsg;
import org.apache.commons.lang.StringUtils;

/**
 * Created on 17/6/10.
 * 语音API产品的DEMO程序,工程中包含了一个VmsDemo类，直接通过
 * 执行main函数即可体验语音产品API功能(只需要将AK替换成开通了云通信-语音产品功能的AK即可)
 * <p>
 * 备注:Demo工程编码采用UTF-8
 */
public class Vms {
    /**
     * 文本转语音外呼
     *
     * @return
     * @throws ClientException
     */
    public static SingleCallByTtsResponse singleCallByTts(SendMsg msg) throws ClientException {
        //可自助调整超时时间
        //System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        //System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", PropertyPlaceholder.getProperty("ali.accessKeyId"), PropertyPlaceholder.getProperty("ali.accessKeySecret"));
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PropertyPlaceholder.getProperty("ali.product"), PropertyPlaceholder.getProperty("ali.domain"));
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber(PropertyPlaceholder.getProperty("ali.calledShowNumber"));
        //必填-被叫号码
        request.setCalledNumber(msg.getPhone());
        //必填-Tts模板ID
        request.setTtsCode(msg.getNumberCode());
        //可选-当模板中存在变量时需要设置此值
        JSONObject jsonObject = new JSONObject();
        boolean bool = false;
        if (StringUtils.isNotBlank(msg.getProduct())) {
            jsonObject = JSONObject.parseObject(msg.getProduct());
            bool = true;
        }
        if (msg.getCode() != null) {
            jsonObject.put("code", msg.getCode());
            bool = true;
        }
        if (bool) {
            request.setTtsParam(JSONObject.toJSONString(jsonObject));
        }
        //可选-播放音量大小
        //request.setVolume(100);
        //可选-播放次数(最多3次)
        //request.setPlayTimes(1);
        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
        //request.setOutId("yourOutId");
        //此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = acsClient.getAcsResponse(request);
        return singleCallByTtsResponse;
    }


}
