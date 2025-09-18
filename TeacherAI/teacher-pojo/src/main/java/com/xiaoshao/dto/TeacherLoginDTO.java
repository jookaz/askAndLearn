package com.xiaoshao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherLoginDTO implements Serializable {
    private String workerNumber;
    private String password;
}
