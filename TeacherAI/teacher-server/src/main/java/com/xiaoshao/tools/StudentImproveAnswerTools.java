package com.xiaoshao.tools;

import com.xiaoshao.entity.StudentImproveAnswer;
import com.xiaoshao.service.IStudentImproveAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Component
public class StudentImproveAnswerTools {
    private final IStudentImproveAnswerService studentImproveAnswerService;

    /**
     * 创建学生改进答案记录
     * @param questionId 关联问题ID
     * @param answerContent 学生改进后的答案内容
     * @return 新记录的主键ID
     */
    @Tool(description = "创建学生改进答案记录")
    public Long createStudentAnswer(
            @ToolParam(description = "关联问题ID") Long questionId,
            @ToolParam(description = "回答学生ID") Long studentId,
            @ToolParam(description = "模型所给评分") Integer modelScore,
            @ToolParam(description = "学生改进后的答案内容") String answerContent
    ) {
        // 创建实体对象并设置参数
        StudentImproveAnswer answer = new StudentImproveAnswer()
                .setQuestionId(questionId)
                .setStudentId(studentId)  // 默认学生ID设为3
                .setAnswerContent(answerContent)
                .setSelfScore(0)    // 默认自我评分0
                .setModelScore(modelScore)   // 默认模型评分0
                .setCreateTime(LocalDateTime.now());  // 自动设置当前时间

        // 调用服务层保存记录
        boolean success = studentImproveAnswerService.save(answer);
        if (!success) {
            throw new RuntimeException("创建学生改进答案记录失败");
        }

        return answer.getId();  // 返回新记录的主键ID
    }
}