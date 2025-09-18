package com.xiaoshao.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserChapterProgressDTO implements Serializable {
    private Long userId;


    private Long courseId;

    private Long chapterId;


    private Integer step;
}

