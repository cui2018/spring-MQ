package com.spring.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @Auther: cui
 * @Date: 2019/1/8 17:54
 * @Description:
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "mobile_msg")
public class Mobile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(25) default \"\"")
    private String voiceNumber; //语音通知码号，非必填,为空时显示平台配置的默认号码
    @Column(columnDefinition = "int(3) default 0")
    private int templateId; //模板id，必填
    @Column(columnDefinition = "varchar(150) default \"\"")
    private String conf; //模板配置
    @Column(columnDefinition = "int(1) default 1")
    private int playTimes; //播放次数
    @Column(columnDefinition = "int(3) default 0")
    private int type; //模板配置类型 0为默认
    @Column(columnDefinition = "int(1) default 0")
    private int state; //是否启用模板（0-不启用  1-启用）注：目前未用此参数
}
