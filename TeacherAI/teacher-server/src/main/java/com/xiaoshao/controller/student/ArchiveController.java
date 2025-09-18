package com.xiaoshao.controller.student;

import com.xiaoshao.controller.teacher.ArchiveQuestionsController;
import com.xiaoshao.dto.ArchiveQuestionsDTO;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IArchiveQuestionsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student/archive")
@Tag(name = "归档接口")
@Slf4j
public class ArchiveController {
    private final IArchiveQuestionsService archiveQuestionsService;
    @GetMapping("/page")
    public Result<?> page(ArchiveQuestionsDTO dto){
        return Result.success(archiveQuestionsService.page(dto));
    }


}
