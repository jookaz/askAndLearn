package com.xiaoshao.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 提示词模板表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-25
 */
@Data
@Accessors(chain = true)
public class PromptTemplateVO implements Serializable {


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
     * 创建人
     */
    private Long updateUser;



}
