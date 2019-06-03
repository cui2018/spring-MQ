package com.properpush.voice.customer.msglistenter;

import com.alibaba.fastjson.JSONObject;
import com.properpush.voice.customer.service.CustomerService;
import com.properpush.voice.producer.reqentity.SendMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Auther: cui
 * @Date: 2018/12/28 13:34
 * @Description: 监听生产的消息
 */
@Slf4j
public class ProducerMessageListener implements MessageListener {

    @Autowired
    CustomerService customerService;

    @Override
    public void onMessage(Message message) {
        SendMsg sendMsg = null;
        try {
            sendMsg = JSONObject.parseObject(((TextMessage) message).getText(), SendMsg.class);
            customerService.saveSendProducer(sendMsg);
        } catch (JMSException e) {
            log.warn("activeMq消息监听失败，语音号：{}，模板ID：{}，用户手机号：{}，异常：{}", sendMsg.getNumberCode(), sendMsg.getTemplateId(),sendMsg.getPhone(), e.toString());
        } catch (Exception e) {
            log.warn("其他异常，语音号：{}，模板ID：{}，用户手机号：{}，异常：{}", sendMsg.getNumberCode(), sendMsg.getTemplateId(), sendMsg.getPhone(), e.toString());
        }
    }
}
