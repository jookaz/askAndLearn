package com.xiaoshao.controller.student;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaoshao.dto.StudentAnswerDTO;
import com.xiaoshao.dto.StudentImproveAnswerDTO;
import com.xiaoshao.entity.StudentAnswer;
import com.xiaoshao.entity.StudentImproveAnswer;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IStudentAnswerService;
import com.xiaoshao.service.IStudentImproveAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 学生提升回答记录 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("student/studentImproveAnswer")
@Slf4j
public class StudentImproveAnswerController {
    private final IStudentImproveAnswerService studentImproveAnswerService;
    /**
     * 根据用户ID更新问题点赞数
     * @param isLike true=点赞，false=取消点赞
     * @return 操作结果
     */
    @PutMapping("/updateLike")
    public Result<?> updateStudentImproveAnswerLike(
            @RequestParam Long improveAnswerId,
            @RequestParam boolean isLike) {
        studentImproveAnswerService.updateLikeNumber(improveAnswerId, isLike);
        return Result.success();
    }
    @Operation(summary = "更新学生别人评价理由")
    @PutMapping("/reason")
    public Result<?> updateStudentReason(@RequestBody StudentImproveAnswerDTO studentImproveAnswerDTO) {

        Long id= studentImproveAnswerDTO.getAnswerId();
        String studentReason =  studentImproveAnswerDTO.getStudentReason();
        boolean success = studentImproveAnswerService.update(
                new LambdaUpdateWrapper<StudentImproveAnswer>()
                        .set(StudentImproveAnswer::getStudentReason,studentReason)
                        .eq(StudentImproveAnswer::getId,id)
        );

        if (!success) {
            log.error("更新学生理由失败，ID: {}", id);
            return Result.error("更新失败");
        }
        log.info("更新理由成功，ID: {}", id);
        return Result.success();
    }


}
