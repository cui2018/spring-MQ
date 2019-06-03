package com.spring.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @Auther: cui
 * @Date: 2019/1/3 10:04
 * @Description:
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "ali_msg")
public class AliMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(25) default \"\"")
    private String ttsCode;
    @Column(columnDefinition = "varchar(150) default \"\"")
    private String productMsg;
    @Column(columnDefinition = "varchar(2) default \"\"")
    private String type;
}
