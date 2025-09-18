package com.xiaoshao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginDTO implements Serializable {
    private String studentNumber;
    private String password;
}
