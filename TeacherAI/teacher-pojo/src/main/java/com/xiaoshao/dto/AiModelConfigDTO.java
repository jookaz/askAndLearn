package com.xiaoshao.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AiModelConfigDTO implements Serializable {
    private Integer id;
    /**
     * 模型名称（唯一标识）
     */
    private String modelName;

    /**
     * API基础地址
     */
    private String baseUrl;

    /**
     * API密钥
     */
    private String apiKey;

//    private Long createUser;
}
