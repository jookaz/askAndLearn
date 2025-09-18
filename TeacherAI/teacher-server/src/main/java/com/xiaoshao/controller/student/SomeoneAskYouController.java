package com.xiaoshao.controller.student;

import com.xiaoshao.dto.AskModelDTO;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.AskYouService;
import com.xiaoshao.vo.StudentAnswerResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student/someoneAsk")
@Tag(name = "他问你答学生提问接口")
@Slf4j
public class SomeoneAskYouController {
    //    由于会使用到你问你答的方法因此这里引入你问你答的逻辑层
    private final AskYouService askYouService;
    // 单模型接口：直接返回完整响应
    @PostMapping("/chat/improveAnswer")
    @Operation(summary = "系统模型确认学生答案")
    public Result<StudentAnswerResponseVO> chatAnswer(
            @RequestBody AskModelDTO askModelDTO
    ) {
        return Result.success(askYouService.systemChatImproveAnswer(
                askModelDTO.getPrompt(), askModelDTO.getUserId(),
                askModelDTO.getCourseId(), askModelDTO.getChapterId(),
                askModelDTO.getQuestion(),askModelDTO.getQuestionId()
        ));
    }
}
