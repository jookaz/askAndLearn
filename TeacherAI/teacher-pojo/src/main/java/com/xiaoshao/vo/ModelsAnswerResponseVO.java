package com.xiaoshao.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ModelsAnswerResponseVO {
    @JsonProperty("response")
    private String response;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("modelName")
    private String modelName;
}
