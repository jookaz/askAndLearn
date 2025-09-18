package com.xiaoshao.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
//该注解会 在序列化阶段（Java对象→JSON） 过滤掉值为 null 的属性，但不会影响反序列化（JSON→Java对象），简单来讲就是讲该类传到前端的时候，系统会自动过滤值为null的属性
@Data
public class ModelQuestionResponseVO {
    @JsonProperty("response")
    private String response;
    @JsonProperty("cognitiveLevel")
    private String cognitiveLevel;
    @JsonProperty("wenxueValue")
    private Integer wenxueValue;
    @JsonProperty("flag")
    private Boolean flag;
//    不需要json解析的属性
    private String question;
    private Long questionId;
}
