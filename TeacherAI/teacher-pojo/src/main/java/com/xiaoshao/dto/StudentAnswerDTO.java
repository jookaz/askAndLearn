package com.xiaoshao.dto;

import lombok.Data;

@Data
public class StudentAnswerDTO {
    private Long answerId;
    private Integer anotherStudentScore;
    private Integer likeNumber;
}
