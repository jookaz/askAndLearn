package com.xiaoshao.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
//支持链式编程
@Builder
public class TeacherLoginVO implements Serializable {
    private Long id;

    private String userName;

    private String teacherName;

    private String title;

    private String department;

    private String token;


}
