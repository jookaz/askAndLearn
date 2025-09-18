package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@Builder
////使用builder必须加上后面两个注解
//@NoArgsConstructor
//@AllArgsConstructor
@TableName("student_answer")
public class StudentAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联问题ID（question表id）
     */
    @TableField("question_id")
    private Long questionId;

    /**
     * 回答学生ID（user_base表id）
     */
    private Long studentId;

    /**
     * 学生改进后的答案
     */
    private String answerContent;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 系统评分（0-10分，默认NULL）
     */
    private Integer systemScore;
    /**
     * 点赞数
     */
    private Integer likeNumber;

    private Integer anotherStudentScore;



}
