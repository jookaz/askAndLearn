package com.xiaoshao.controller.teacher;


import com.xiaoshao.dto.CourseDTO;
import com.xiaoshao.dto.QuestionDTO;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IQuestionService;
import com.xiaoshao.vo.QuestionVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-27
 */
@RestController("teacherQuestionController")
@RequestMapping("teacher/question")
@AllArgsConstructor
public class QuestionController {
    private final IQuestionService questionService;
    /**
     * 分页查询
     * @param questionDTO
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<?> page(QuestionDTO questionDTO) {
        return Result.success(questionService.queryQuestionsPage(questionDTO));
    }
    /**
     * 获取问题的多个答案信息
     */
    @GetMapping("/allAnswer")
    public Result<QuestionVO> getAllAnswer(@RequestParam Long questionId) {
        QuestionVO vo = questionService.getQuestionDetailWithAnswers(questionId);
        return vo != null ? Result.success(vo) : Result.error("问题不存在");
    }
}

