package com.xiaoshao.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 学生章节问学值表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-02
 */
@Data
public class StudentChapterWenxueValueDTO implements Serializable {





    /**
     * 学生ID（关联student.id）
     */
    private Long studentId;

    /**
     * 课程ID（关联course.id）
     */
    private Long courseId;






}
