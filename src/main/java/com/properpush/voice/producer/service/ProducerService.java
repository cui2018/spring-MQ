package com.properpush.voice.producer.service;

import com.properpush.util.Result;
import com.properpush.voice.producer.reqentity.SendMsg;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2018/12/17 23:28
 * @Description:
 */
public interface ProducerService {
    Result sendMessage(SendMsg msg);
}
