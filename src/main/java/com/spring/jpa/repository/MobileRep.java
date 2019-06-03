package com.spring.jpa.repository;

import com.spring.jpa.entity.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: cui
 * @Date: 2019/1/8 18:01
 * @Description:
 */
@Repository
public interface MobileRep extends JpaRepository<Mobile, Integer> {
    List<Mobile> findByVoiceNumberAndTemplateIdAndType(String numberCode, int templateId, int type);
}
