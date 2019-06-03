package com.spring.jpa.repository;

import com.spring.jpa.entity.ProducerQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Auther: cui
 * @Date: 2018/12/26 18:14
 * @Description:
 */
@Repository
public interface ProducerQueueRep extends JpaRepository<ProducerQueue, Integer> {
}
