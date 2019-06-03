package com.properpush.util;

import com.properpush.voice.producer.reqentity.SendMsg;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: cui
 * @Date: 2019/1/17 17:50
 * @Description:
 */
@Slf4j
@Aspect
public class ControllerInterceptor {

    @Around("execution(* com.properpush.voice.producer.controller.*.*(..))")
    public Object around(ProceedingJoinPoint point) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            if (checkSign((SendMsg) point.getArgs()[0])) {
                return new ResponseEntity(new Result(ResultEnum.SIGNERROR), HttpStatus.OK);
            }
            try {
                return point.proceed();
            } catch (Throwable throwable) {
                return new ResponseEntity(new Result(ResultEnum.UNKNOWNERROR), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity(new Result(ResultEnum.SIGNERROR), HttpStatus.OK);
        }
    }

    private boolean checkSign(SendMsg sendMsg) {
        String signStr = MD5Util.getMd5(sendMsg.getPhone() + sendMsg.getNumberCode() + System.currentTimeMillis()).substring(4);
        if (signStr.equalsIgnoreCase(sendMsg.getCode())) {
            return false;
        }
        log.warn("加密错误：{}", sendMsg);
        return true;
    }

}
