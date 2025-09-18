package com.xiaoshao.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "teacher.jwt")
@Data
public class JwtProperties {

    /**
     * 学生生成jwt令牌相关配置
     */
    private String studentSecretKey;
    private long studentTtl;
    private String studentTokenName;

    /**
     * 教师生成jwt令牌相关配置
     */
    private String teacherSecretKey;
    private long teacherTtl;
    private String teacherTokenName;
    /**
     * 管理员生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

}
