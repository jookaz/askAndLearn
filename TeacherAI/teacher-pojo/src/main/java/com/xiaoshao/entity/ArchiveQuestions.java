package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 问题归档表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("archive_questions")
public class ArchiveQuestions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 所属课程ID（逻辑外键）
     */
    private Long courseId;
    private Long chapterId;
    /**
     * 原问题ID（用于追溯）
     */
    private Long originalQuestionId;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT,value = "create_user")
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String remark;


}
