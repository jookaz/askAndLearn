package com.xiaoshao.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class ArchiveAnswersDTO implements Serializable {
    /**
     * 答案内容
     */
    private String content;

    /**
     * 原问题ID，用于后续获取修改问题的id
     */
    private Long questionId;
}
