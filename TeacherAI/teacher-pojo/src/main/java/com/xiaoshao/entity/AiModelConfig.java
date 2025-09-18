package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * AI模型配置表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ai_model_config")
public class AiModelConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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



    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @TableField(fill= FieldFill.INSERT, value = "create_user")
    private Long createUser;
    /**
     * 启用标记
     */
    private Integer systemFlag;


}
