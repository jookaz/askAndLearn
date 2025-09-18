package com.xiaoshao.dto;

import com.xiaoshao.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionDTO extends PageQuery implements Serializable {
    private Long id;
    private Long courseId;
    private Long chapterId;
    private Integer likeNumber;
    private Long studentId;
    private String questionContent; // 新增：问题内容模糊查询
    private String cognitiveLevel;  // 新增：认知层次精确查询

}
//    private String questionContent;
//    private String cognitiveLevel;