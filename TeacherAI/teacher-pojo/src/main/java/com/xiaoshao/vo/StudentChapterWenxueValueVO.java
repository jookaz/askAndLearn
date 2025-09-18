package com.xiaoshao.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 学生章节问学值表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-02
 */
@Data
public class StudentChapterWenxueValueVO implements Serializable {



    /**
     * 主键
     */
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



}
