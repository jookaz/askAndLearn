package com.xiaoshao.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArchiveQuestionsVO implements Serializable {

    private Long id;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 所属课程ID（逻辑外键）
     */
    private Long courseId;
    /**
     * 所属章节ID（逻辑外键）
     */
    private Long chapterId;
    /**
     * 相关联回答
     */
    private String answerContent;



}
