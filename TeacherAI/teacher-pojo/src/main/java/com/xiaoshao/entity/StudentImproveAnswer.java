package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaoshao.dto.StudentAnswerDTO;
import com.xiaoshao.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 学生提升回答记录
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student_improve_answer")
public class StudentImproveAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联的问题ID
     */
    private Long questionId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 回答内容
     */
    private String answerContent;

    /**
     * 自我评分（默认0）
     */
    private Integer selfScore;

    /**
     * 大模型评分（默认0）
     */
    private Integer modelScore;
    /**
     * 点赞数
     */
    private Integer likeNumber;
    /**
     * 学生理由
     */
    private String studentReason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
