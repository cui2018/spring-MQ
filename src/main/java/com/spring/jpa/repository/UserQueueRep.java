package com.spring.jpa.repository;

import com.spring.jpa.entity.UserQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Auther: cui
 * @Date: 2018/12/26 18:15
 * @Description:
 */
@Repository
public interface UserQueueRep extends JpaRepository<UserQueue, Integer> {
}
