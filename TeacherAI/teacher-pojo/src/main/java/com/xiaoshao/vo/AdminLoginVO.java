package com.xiaoshao.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
//支持链式编程
@Builder
public class AdminLoginVO implements Serializable {
    private String adminName;
    private Integer id;
    private String token;
}
