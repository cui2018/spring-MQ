package com.properpush.voice.customer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.properpush.mobile.ChinaMobile;
import com.properpush.mobile.Resp.MobileResp;
import com.properpush.voice.customer.service.CustomerService;
import com.properpush.voice.producer.reqentity.SendMsg;
import com.spring.jpa.entity.ProducerQueue;
import com.spring.jpa.entity.UserQueue;
import com.spring.jpa.repository.ProducerQueueRep;
import com.spring.jpa.repository.UserQueueRep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.Destination;

/**
 * @Auther: cui
 * @Date: 2018/12/24 13:50
 * @Description:
 */
@Slf4j
@Service
public class CustomerImpl implements CustomerService {

    @Autowired
    JmsTemplate jmsTemplate;
    @Resource(name = "customerqueue")
    Destination destination;
    @Autowired
    private UserQueueRep userQueueRep;
    @Autowired
    private ProducerQueueRep producerQueueRep;


    @Transactional
    @Override
    public void sendMsg(SendMsg msg) throws Exception {
        //语音服务消息通知用户
        MobileResp mobileResp = ChinaMobile.sendMobile(msg);
        //MobileResp mobileResp = null;
        //用户服务信息
        UserQueue user;
        //语音服务返回描述
        if (mobileResp != null) {
            user = new UserQueue();
            if (mobileResp.getResultCode() == 200) {
                user.setState(1);
                log.info("语音服务发送成功，语音模板：{}，用户手机号：{}，移动语音服务返回：{}", msg.getNumberCode(), msg.getPhone(), JSONObject.toJSONString(mobileResp));
            }
            user.setMsg(mobileResp.getResultCode() + "-" + mobileResp.getResultMsg() + "-" + mobileResp.getCallId());
            user.setPhone(msg.getPhone().length() > 11 ? msg.getPhone().substring(0, 11) : msg.getPhone());
            user.setNumberCode(msg.getNumberCode());
            userQueueRep.save(user);
            log.info("发送语音消息保存成功：{}",user);
        } else {
            if (msg.getSendNum() < 2) {
                msg.setSendNum(msg.getSendNum() + 1);
                jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(msg));
                log.warn("语音服务发送出现异常重发一次，语音号：{}，模板ID：{}，用户手机号：{}", msg.getNumberCode(), msg.getTemplateId(), msg.getPhone());
            } else {
                //发生异常重发一次还未成功发送语音服务
                user = new UserQueue();
                user.setState(2);
                user.setPhone(msg.getPhone().length() > 11 ? msg.getPhone().substring(0, 11) : msg.getPhone());
                user.setNumberCode(msg.getNumberCode());
                user.setMsg(JSONObject.toJSONString(msg));
                userQueueRep.save(user);
                log.warn("语音服务发送出现异常重发一次后，还是失败，语音号：{}，模板ID：{}，用户手机号：{}", msg.getNumberCode(), msg.getTemplateId(), msg.getPhone());
            }
        }
    }

    @Transactional
    @Override
    public void saveSendProducer(SendMsg msg){
        ProducerQueue producerMsg = new ProducerQueue();
        BeanUtils.copyProperties(msg, producerMsg);
        producerMsg.setPhone(producerMsg.getPhone().length() > 11 ? producerMsg.getPhone().substring(0, 11) : producerMsg.getPhone());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(msg));
        jsonObject.fluentRemove("phone");
        jsonObject.fluentRemove("produceTime");
        jsonObject.fluentRemove("numberCode");
        producerMsg.setMsg(JSONObject.toJSONString(jsonObject));
        producerQueueRep.save(producerMsg);
        log.info("生产的消息保存成功：{}",producerMsg);
    }
}
