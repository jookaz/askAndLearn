package com.xiaoshao.dto;

import com.xiaoshao.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseDTO extends PageQuery implements Serializable  {
    private Long id;

    private Long teacherId;

    private String description;

    private String courseName;

    private String image;

    private List<ChapterDTO> chapters; // 新增章节列表字段

}
