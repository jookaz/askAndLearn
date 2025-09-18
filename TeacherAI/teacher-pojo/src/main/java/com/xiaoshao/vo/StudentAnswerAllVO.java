package com.xiaoshao.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class StudentAnswerAllVO implements Serializable {

    private Long id;

    /**
     * 关联问题ID（question表id）
     */
    private Long questionId;

    /**
     * 回答学生ID（user_base表id）
     */
    private Long studentId;
    private String studentName;
    private String classes;

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
