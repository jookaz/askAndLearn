package com.xiaoshao.controller.student;

import com.xiaoshao.dto.AskModelDTO;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.AskYouService;
import com.xiaoshao.vo.ModelQuestionResponseVO;
import com.xiaoshao.vo.ModelsAnswerResponseVO;
import com.xiaoshao.vo.StudentAnswerResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student/youAsk")
@Tag(name = "你问你答学生提问接口")
@Slf4j
//TODO进行会话历史的展示以及持久化存储
//TODO错误处理，有时候模型的返回值并不是按我们的要求完成的，那么这个时候就会报错，那么应该如何处理错误
public class AskYouController {

    private final AskYouService askYouService;

    // 单模型接口：直接返回完整响应
    @PostMapping("/chat")
    @Operation(summary = "系统模型确认学生问题")
    public Result<ModelQuestionResponseVO> chat(
            @RequestBody AskModelDTO askModelDTO
    ) {
        return Result.success(askYouService.systemChat(
                askModelDTO.getPrompt(), askModelDTO.getUserId(),
                askModelDTO.getCourseId(), askModelDTO.getChapterId()
        ));
    }
    // 单模型接口：直接返回完整响应
    @PostMapping("/chat/answer")
    @Operation(summary = "系统模型确认学生答案")
    public Result<StudentAnswerResponseVO> chatAnswer(
            @RequestBody AskModelDTO askModelDTO
    ) {
        return Result.success(askYouService.systemChatAnswer(
                askModelDTO.getPrompt(), askModelDTO.getUserId(),
                askModelDTO.getCourseId(), askModelDTO.getChapterId(),
                askModelDTO.getQuestion(),askModelDTO.getQuestionId()
        ));
    }


    // 多模型接口：同步返回所有模型的完整响应
    @PostMapping("/chat/multi")
    @Operation(summary = "学生提问接口（多模型并行完整响应）")
    public Result<List<ModelsAnswerResponseVO>> multiModelResponse(
            @RequestBody AskModelDTO askModelDTO
    ) {
        return Result.success(askYouService.multiModelChat(
                askModelDTO.getModelNames(), askModelDTO.getQuestion(),
                askModelDTO.getUserId(), askModelDTO.getCourseId(),
                askModelDTO.getChapterId(), askModelDTO.getQuestionId()));
    }
    // 获取支持的模型列表
    @GetMapping("/models/{courseId}")
    @Operation(summary = "获取支持的模型列表")
    public Result<List<String>> getAvailableModels(@PathVariable Long courseId) {
        return Result.success(askYouService.getAvailableModels(courseId));
    }
}
