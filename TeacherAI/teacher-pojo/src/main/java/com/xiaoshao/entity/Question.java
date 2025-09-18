package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联课程ID（course表id）
     */
    private Long courseId;

    /**
     * 关联章节ID（chapter表id）
     */
    private Long chapterId;

    /**
     * 提问学生ID（student表id）
     */
    private Long studentId;

    /**
     * 问题内容
     */
    private String questionContent;

    /**
     * 认知层次（如记忆、理解、应用等）
     */
    private String cognitiveLevel;
    /**
     * 是否被选中
     */
    private Integer ifSelect;
    /**
     * 点赞数
     */
    private Integer likeNumber;

    /**
     * 提问时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,  value = "update_time")
    private LocalDateTime updateTime;


}
