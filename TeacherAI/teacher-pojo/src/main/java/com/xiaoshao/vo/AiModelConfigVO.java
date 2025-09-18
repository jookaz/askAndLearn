package com.xiaoshao.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class AiModelConfigVO implements Serializable {
    private Integer id; // 新增主键ID用于前端操作
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

    private Integer systemFlag;
}
