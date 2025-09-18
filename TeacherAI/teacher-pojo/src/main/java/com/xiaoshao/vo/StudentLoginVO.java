package com.xiaoshao.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
//支持链式编程
@Builder
public class StudentLoginVO implements Serializable {
    private Long id;

    private String studentName;

    private String classes;

    private String major;
    /**
     * 用户名称
     */
    private String userName;
    private String token;


}
