package com.xiaoshao.controller.student;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaoshao.dto.CourseDTO;
import com.xiaoshao.dto.ModelsAnswerDTO;
import com.xiaoshao.entity.ModelAnswer;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.AskYouService;
import com.xiaoshao.service.IModelAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student/modelsAnswer")
@Tag(name = "学生端获取模型答案端口")
@Slf4j
    public class ModelsAnswerController {
        private final IModelAnswerService modelAnswerService;
    /**
     * 更新模型分数
     * @param  modelsAnswerDTO
     * @return 操作结果
     */
    @Operation(summary = "更新模型分数")
    @PutMapping("/score")
    public Result<?> updateStudentScore(@RequestBody ModelsAnswerDTO modelsAnswerDTO) {

        log.info("开始更新模型评分，ID: {}, 评分: {}", modelsAnswerDTO.getId(), modelsAnswerDTO.getStudentScore());
        Long id=modelsAnswerDTO.getId();
        Integer studentScore = modelsAnswerDTO.getStudentScore();
        boolean success = modelAnswerService.update(
                new LambdaUpdateWrapper<ModelAnswer>()
                        .set(ModelAnswer::getStudentScore, studentScore)
                        .eq(ModelAnswer::getId,id)
        );

        if (!success) {
            log.error("更新模型评分失败，ID: {}", id);
            return Result.error("更新失败");
        }
        log.info("更新模型评分成功，ID: {}", id);
        return Result.success();
    }
    /**
     * 更新模型分数
     * @param  modelsAnswerDTO
     * @return 操作结果
     */
    @Operation(summary = "更新模型理由")
    @PutMapping("/reason")
    public Result<?> updateStudentReason(@RequestBody ModelsAnswerDTO modelsAnswerDTO) {

        log.info("开始更新模型理由，ID: {}, 理由: {}", modelsAnswerDTO.getId(), modelsAnswerDTO.getStudentReason());
        Long id=modelsAnswerDTO.getId();
        String studentReason = modelsAnswerDTO.getStudentReason();
        boolean success = modelAnswerService.update(
                new LambdaUpdateWrapper<ModelAnswer>()
                        .set(ModelAnswer::getStudentReason, studentReason)
                        .eq(ModelAnswer::getId,id)
        );

        if (!success) {
            log.error("更新模型理由失败，ID: {}", id);
            return Result.error("更新失败");
        }
        log.info("更新模型理由成功，ID: {}", id);
        return Result.success();
    }
}
