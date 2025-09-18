package com.xiaoshao.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 提示词模板表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-25
 */
@Data
public class PromptTemplateDTO implements Serializable {


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 提示词名称
     */
    private String promptName;

    /**
     * 提示词内容（支持长文本）
     */
    private String promptContent;



}
