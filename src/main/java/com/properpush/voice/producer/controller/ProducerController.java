package com.properpush.voice.producer.controller;

import com.properpush.util.Result;
import com.properpush.util.ResultEnum;
import com.properpush.voice.producer.reqentity.SendMsg;
import com.properpush.voice.producer.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Auther: cui
 * @Date: 2018/12/18 11:43
 * @Description: 增加消息控制类
 */
@Slf4j
@RestController
@RequestMapping(value = "/producer", produces = "application/json;utf-8")
public class ProducerController {
    @Autowired
    ProducerService producerService;

    /**
     * 新增消息
     */
    @RequestMapping(value = "/add/queue", method = RequestMethod.POST)
    public ResponseEntity addQueue(@RequestBody SendMsg msg) {
        msg.setProduceTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        Result result;
        if (StringUtils.isBlank(msg.getTemplateId()) || StringUtils.isBlank(msg.getPhone())) {
            result = new Result(ResultEnum.PARMERROR);
            log.warn("参数有误：{}", msg);
        } else {
            result = producerService.sendMessage(msg);
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

}
