package com.xiaoshao.tools;

import com.xiaoshao.entity.ModelAnswer;
import com.xiaoshao.service.IModelAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ModelsAnswerTools {
    private final IModelAnswerService modelAnswerService;
    @Tool(description = "创建大模型回答记录")
    public Long createModelAnswer(
            @ToolParam(description = "关联问题ID") Long questionId,
            @ToolParam(description = "大模型回答内容") String answerContent,
            @ToolParam(description = "使用的模型名称（如qwen-turbo）") String modelUsed
    ) {
        // 创建实体对象
        ModelAnswer modelAnswer = new ModelAnswer();
        modelAnswer.setQuestionId(questionId);
        modelAnswer.setAnswerContent(answerContent);
        modelAnswer.setModelUsed(modelUsed);
        modelAnswer.setAnswerTime(LocalDateTime.now()); // 自动设置当前时间
        modelAnswer.setStudentScore(0); // 可选参数，默认为NULL

        // 调用服务层保存记录
        boolean success = modelAnswerService.save(modelAnswer);
        if (!success) {
            throw new RuntimeException("创建模型回答记录失败");
        }

        return modelAnswer.getId(); // 返回新记录的主键ID
    }
    @Tool(description = "查询大模型回答记录")
    public String getModelAnswerById(
            @ToolParam(description = "大模型回答记录的主键ID") Long id
    ) {
        // 通过ID查询记录
        return modelAnswerService.getById(id).getAnswerContent();
    }

}
