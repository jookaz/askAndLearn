package com.xiaoshao.dto;

import com.xiaoshao.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChapterDTO  extends PageQuery implements Serializable {
    private Long id;

    private Long courseId;      // 对应数据库字段 course_id（外键）
    private String chapterName; // 对应 chapter_name
    private Integer chapterOrder; // 对应 chapter_order（整型）

    // 可选：如果需要传递时间字段（如更新时）
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}