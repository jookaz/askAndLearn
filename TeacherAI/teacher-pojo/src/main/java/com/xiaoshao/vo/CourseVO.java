package com.xiaoshao.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CourseVO implements Serializable {
    private Long id;

    private Long teacherId;

    private String teacherName;  // 教师名称

    private String courseName;

    private String description;

    private String image;

    private List<ChapterVO> chapters; // 新增章节列表字段

}
