package com.xiaoshao.dto;

import com.xiaoshao.query.PageQuery;
import lombok.Data;

import java.io.Serializable;
@Data
public class ArchiveQuestionsDTO extends PageQuery implements Serializable {
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
     * 原问题ID（用于追溯）
     */
    private Long originalQuestionId;

}
