package com.xiaoshao.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChapterVO implements Serializable {
    private Long id;

    private Long courseId;      // 对应 course_id
    private String chapterName;
    private String courseName;
    private Integer chapterOrder;
    
    // 时间字段（通常在视图中展示）
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}