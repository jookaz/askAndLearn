package com.xiaoshao.tools;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.xiaoshao.entity.Question;
import com.xiaoshao.service.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问题相关工具类，供大模型调用
 */
@Component
@RequiredArgsConstructor
public class QuestionTools {

    private final IQuestionService questionService;


    /**
     * 创建新问题
     *
     * @param questionContent    问题内容
     * @param courseId           关联课程ID
     * @param chapterId          关联章节ID
     * @param studentId          提问学生ID
     * @param cognitiveLevel     认知层次（如记忆、理解、应用等）
     * @return 新问题的ID
     */
    @Tool(description = "创建新问题")
//    给模型的提示词上传两个内容一个就是id一个就是名字，以及前端只需要上传id，后续name需要后端自己调用业务层进行查询
    public Long createQuestion(
        @ToolParam(description = "问题内容") String questionContent,
        @ToolParam(description = "关联课程ID") Long courseId,
        @ToolParam(description = "关联章节ID") Long chapterId,
        @ToolParam(description = "提问学生ID") Long studentId,
        @ToolParam(description = "认知层次（如记忆、理解、应用等）", required = false) String cognitiveLevel
    ) {
        Question question = new Question();
        question.setQuestionContent(questionContent);
        question.setCourseId(courseId);
        question.setChapterId(chapterId);
        question.setStudentId(studentId);
        question.setCognitiveLevel(cognitiveLevel);

        boolean success = questionService.save(question);
        if (!success) {
            throw new RuntimeException("创建问题失败");
        }

        return question.getId(); // 返回新问题的ID
    }
}