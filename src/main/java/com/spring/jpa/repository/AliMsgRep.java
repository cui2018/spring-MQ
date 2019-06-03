package com.spring.jpa.repository;

import com.spring.jpa.entity.AliMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: cui
 * @Date: 2019/1/3 15:36
 * @Description:
 */
@Repository
public interface AliMsgRep extends JpaRepository<AliMsg, Integer> {
    List<AliMsg> findByTtsCodeAndType(String ttsCode, String type);
}
