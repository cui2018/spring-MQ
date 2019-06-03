package com.properpush.voice.customer.service;

import com.properpush.voice.producer.reqentity.SendMsg;
import org.springframework.stereotype.Service;

/**
 * @Auther: cui
 * @Date: 2018/12/29 12:48
 * @Description:
 */
@Service
public interface CustomerService {
    void sendMsg(SendMsg msg) throws Exception;
    void saveSendProducer(SendMsg msg) throws Exception;
}
