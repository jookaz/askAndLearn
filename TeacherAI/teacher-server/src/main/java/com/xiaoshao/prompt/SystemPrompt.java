package com.xiaoshao.prompt;

import com.xiaoshao.service.IPromptTemplateService;
import com.xiaoshao.vo.PromptTemplateVO;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class SystemPrompt {
    @Autowired
    private IPromptTemplateService promptTemplateService;

    private String singleModelSystemPrompt;
    private String singleModelSystemPromptAnswer;
    private String singleModelImproveAnswer;
    private String multiModelSystemPrompt;

    @PostConstruct // 在所有依赖注入完成后执行
    public void init() {
        this.singleModelSystemPrompt = getPromptContent("SINGLE_MODEL_SYSTEM_PROMPT");
        this.singleModelSystemPromptAnswer = getPromptContent("SINGLE_MODEL_SYSTEM_PROMPT_ANSWER");
        this.singleModelImproveAnswer = getPromptContent("SINGLE_MODEL_IMPROVE_ANSWER");
        this.multiModelSystemPrompt = getPromptContent("MULTI_MODEL_SYSTEM_PROMPT");
    }

    private String getPromptContent(String promptName) {
        PromptTemplateVO vo = promptTemplateService.getDetailByName(promptName);
        return vo != null ? vo.getPromptContent() : "";
    }
}
