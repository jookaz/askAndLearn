package com.xiaoshao.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class StudentDTO implements Serializable {
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 学号
     */
    private String studentNumber;
}
