package com.xiaoshao.tools;

import com.xiaoshao.entity.StudentAnswer;
import com.xiaoshao.service.IStudentAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
public class StudentAnswerTools {
    private final IStudentAnswerService studentAnswerService;

    /**
     * 创建学生回答记录
     * @param questionId 关联问题ID
     * @param studentId 回答学生ID
     * @param answerContent 学生改进后的答案
     * @return 新记录的主键ID
     */
    @Tool(description = "创建学生回答记录")
    public Long createStudentAnswer(
            @ToolParam(description = "关联问题ID") Long questionId,
            @ToolParam(description = "回答学生ID") Long studentId,
            @ToolParam(description = "学生改进后的答案") String answerContent,
            @ToolParam(description = "模型所给评分") Integer systemScore
    ) {
        // 创建实体对象
        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setQuestionId(questionId);
        studentAnswer.setStudentId(studentId);
        studentAnswer.setAnswerContent(answerContent);
        studentAnswer.setSubmitTime(LocalDateTime.now()); // 自动设置当前时间
        studentAnswer.setSystemScore(systemScore); // 默认系统评分设为0

        // 调用服务层保存记录
        boolean success = studentAnswerService.save(studentAnswer);
        if (!success) {
            throw new RuntimeException("创建学生回答记录失败");
        }

        return studentAnswer.getId(); // 返回新记录的主键ID
    }

    /**
     * 查询学生回答记录
     * @param id 学生回答记录的主键ID
     * @return 学生回答内容（若未找到则返回null）
     */
    @Tool(description = "查询学生回答记录")
    public String getStudentAnswerById(
            @ToolParam(description = "学生回答记录的主键ID") Long id
    ) {
        // 通过ID查询记录
        StudentAnswer studentAnswer = studentAnswerService.getById(id);
        return studentAnswer != null ? studentAnswer.getAnswerContent() : null;
    }
}
