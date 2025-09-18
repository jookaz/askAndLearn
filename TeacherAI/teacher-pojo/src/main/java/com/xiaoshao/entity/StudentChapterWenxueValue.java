package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生章节问学值表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student_chapter_wenxue_value")
public class StudentChapterWenxueValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生ID（关联student.id）
     */
    private Long studentId;

    /**
     * 课程ID（关联course.id）
     */
    private Long courseId;


    /**
     * 问学值（初始为0）
     */
    private Integer wenxueValue;

    /**
     * 记录创建时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,  value = "update_time")
    private LocalDateTime updateTime;


}
