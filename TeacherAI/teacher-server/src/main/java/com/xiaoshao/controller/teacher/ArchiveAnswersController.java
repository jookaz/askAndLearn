package com.xiaoshao.controller.teacher;


import com.xiaoshao.dto.ArchiveAnswersDTO;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IArchiveAnswersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 答案归档表 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-19
 */
@RestController
@RequestMapping("/teacher/archive-answers")
@RequiredArgsConstructor
public class ArchiveAnswersController {
    private final IArchiveAnswersService archiveAnswersService;
    @PostMapping
    public Result<Long> createAnswer(@RequestBody ArchiveAnswersDTO dto) {
        // 参数校验通过后，调用Service层
        Long answerId = archiveAnswersService.createAnswer(dto);
        return Result.success(answerId);
    }

}
