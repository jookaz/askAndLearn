package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 答案归档表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("archive_answers")
public class ArchiveAnswers implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 答案内容
     */
    private String content;

    /**
     * 原问题ID，用于后续获取修改问题的id
     */
    private Long questionId;

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


}
