package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2025-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student_answer_all")
public class StudentAnswerAll implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联问题ID（question表id）
     */
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
     * 点赞数
     */
    private Integer likeNumber;


}
