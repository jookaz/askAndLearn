package com.xiaoshao.vo;

import com.xiaoshao.entity.StudentAnswerAll;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionVO implements Serializable {
    private Long id;
    private Long answerId;
    private String questionContent;
    private String answerContent;
    private Integer answerLikeNumber;
    private LocalDateTime answerSubmitTime;
    private Long answerStudentId;
    private String answerStudentName;
    private String answerStudentClasses;
    private Long improveAnswerId;
    private String improveAnswerContent;
    private Integer improveAnswerLikeNumber;
    private LocalDateTime improveAnswerSubmitTime;
    private Long improveAnswerStudentId;
    private String improveAnswerStudentName;
    private String improveAnswerStudentClasses;
    private List<StudentAnswerAllVO> studentAnswerAllVOList;
    /**
     * 认知层次（如记忆、理解、应用等）
     */
    private String cognitiveLevel;

    /**
     * 点赞数
     */
    private Integer likeNumber;
//    章节以及课程id
    private Long chapterId;
    private Long courseId;

//    章节以及课程名称
    private String chapterName;
    private String courseName;
//    后续可能会使用，这里先不写
    private LocalDateTime createTime; // 问题提交时间
//    问题提出者信息
    private Long studentId;
    private String studentName;
    private String classes;

//    private Integer systemScore;    // 系统评分（可选）
}
