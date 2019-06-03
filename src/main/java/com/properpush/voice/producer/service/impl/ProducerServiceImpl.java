package com.properpush.voice.producer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.properpush.util.PropertyPlaceholder;
import com.properpush.util.Result;
import com.properpush.util.ResultEnum;
import com.properpush.voice.producer.reqentity.SendMsg;
import com.properpush.voice.producer.service.ProducerService;
import com.spring.jpa.entity.Mobile;
import com.spring.jpa.repository.MobileRep;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Iterator;
import java.util.List;

/**
 * @Auther: cui
 * @Date: 2018/12/17 23:57
 * @Description: 生产消息
 */
@Slf4j
@Service
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    JmsTemplate jmsTemplate;
    @Resource(name = "allqueue")
    Destination destination;
    @Autowired
    private MobileRep mobileMsgRep;

    @Override
    public Result sendMessage(SendMsg msg) {
        if(StringUtils.isBlank(msg.getNumberCode())){ //判断是否用默认语音号
            msg.setNumberCode(PropertyPlaceholder.getProperty("Mobile.voiceNumber"));
        }
        if(StringUtils.isBlank(msg.getType())){ //判断是否用默认的配置
            msg.setType("0");
        }
        List<Mobile> mobileMsgList = mobileMsgRep.findByVoiceNumberAndTemplateIdAndType(msg.getNumberCode(), Integer.parseInt(msg.getTemplateId().trim()), Integer.parseInt(msg.getType().trim()));
        if (mobileMsgList.isEmpty() || mobileMsgList == null) {
            log.warn("模板参数有误，语音号：{}，模板ID：{}，模板类型：{}",msg.getNumberCode(), msg.getTemplateId(), msg.getType());
            return new Result(ResultEnum.MOULDERROR);
        }
        if(StringUtils.isNotBlank(mobileMsgList.get(0).getConf())){ //判断此语音模板ID配置是否为空
            JSONObject jsonObj= JSONObject.parseObject(mobileMsgList.get(0).getConf());
            //不能为空
            if(msg.getObj() == null){ //发送语音为空时，赋值已配置的信息
                msg.setObj(jsonObj);
            }else{
                boolean bool = compareJson(msg.getObj(), jsonObj);
                if(!bool){
                    log.warn("模板ID配置参数有误，语音号：{}，模板ID：{}，配置参数：{}，模板类型：{}",msg.getNumberCode(), msg.getTemplateId(), msg.getObj(),msg.getType());
                    return new Result(ResultEnum.CONFPARMERROR);
                }
            }
        }else { //配置中已为空，传来数据清空（防止无法发送语音服务）
            msg.setObj(null);
        }
        if(StringUtils.isBlank(msg.getPlayTimes())){
            msg.setPlayTimes(String.valueOf(mobileMsgList.get(0).getPlayTimes()));
        }
        //使用jmsTemplate发送消息
        try {
            msg.setSendNum(1);
            jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(msg));
            log.info("生产消息成功，语音号：{}，模板ID：{}，用户手机号：{}", msg.getNumberCode(), msg.getTemplateId(),msg.getPhone());
            return new Result(ResultEnum.SUCCESS);
        } catch (Exception e) {
            log.warn("生产消息失败，语音号：{}，模板ID：{}，用户手机号：{}，异常：{}", msg.getNumberCode(), msg.getTemplateId(),msg.getPhone(), e);
            return new Result(ResultEnum.ERROR);
        }
    }
    /**
     *@Description 判断jsonObject中key是否一致
     *@Params
     *@Return
     *@Author  cui
     *@Date  2019/1/9
     */
    private boolean compareJson(JSONObject json1, JSONObject json2) {
        Iterator<String> i = json1.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next();
            boolean bool = json2.containsKey(key);
            if(!bool){
                return false;
            }
        }
        return true;
    }
}
