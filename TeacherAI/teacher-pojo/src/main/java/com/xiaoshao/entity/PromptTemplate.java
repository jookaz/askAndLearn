package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 提示词模板表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prompt_template")
public class PromptTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 修改者用户ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_user")
    private Long updateUser;

    /**
     * 修改时间（自动更新）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,  value = "update_time")
    private LocalDateTime updateTime;


}
