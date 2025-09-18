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
 * @since 2025-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("model_answer")
public class ModelAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联问题ID（question表id）
     */
    private Long questionId;

    /**
     * 大模型回答内容
     */
    private String answerContent;

    /**
     * 使用的模型名称（如qwen-turbo）
     */
    private String modelUsed;

    /**
     * 回答时间
     */
    private LocalDateTime answerTime;

    /**
     * 学生评分（0-10分，默认NULL）
     */
    private Integer studentScore;
    /**
     * 学生理由（默认NULL）
     */
    private String studentReason;


}
