package com.xiaoshao.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class StudentAnswerAllDTO implements Serializable {
    /**
     * 关联问题ID（question表id）
     */
    private Long questionId;

    /**
     * 学生改进后的答案
     */
    private String answerContent;



}
