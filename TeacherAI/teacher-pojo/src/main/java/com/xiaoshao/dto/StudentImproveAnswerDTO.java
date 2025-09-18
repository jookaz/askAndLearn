package com.xiaoshao.dto;

import lombok.Data;

@Data
public class StudentImproveAnswerDTO {
    private Long answerId;
    private String studentReason;
    private Integer likeNumber;
}
