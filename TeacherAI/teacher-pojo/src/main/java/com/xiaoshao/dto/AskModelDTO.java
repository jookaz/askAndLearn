package com.xiaoshao.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AskModelDTO implements Serializable {
    private String question;
    private String modelName;
    private String prompt;
    private List<String> modelNames;
    private Long courseId;
    private Long chapterId;
    //TODO  完成登录功能后需要把这里解决一下，目前是前端将用户ID传过来，后续完成JWT令牌功能后可以直接在后端获取用户ID
//    这里的userId其实也可以看作为studentId
    private Long userId;
    private Long questionId;
}
