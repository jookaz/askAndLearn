package com.xiaoshao.controller.student;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaoshao.dto.StudentAnswerDTO;
import com.xiaoshao.entity.StudentAnswer;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IStudentAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("student/studentAnswer")
@RequiredArgsConstructor
@Slf4j
public class StudentAnswerController {
    private final IStudentAnswerService studentAnswerService;
    @Operation(summary = "更新学生别人评价分数")
    @PutMapping
    public Result<?> updateAnotherStudentScore(@RequestBody StudentAnswerDTO studentAnswerDTO) {

        Long id= studentAnswerDTO.getAnswerId();
        Integer anotherStudentScore =  studentAnswerDTO.getAnotherStudentScore();
        boolean success = studentAnswerService.update(
                new LambdaUpdateWrapper<StudentAnswer>()
                        .set(StudentAnswer::getAnotherStudentScore, anotherStudentScore)
                        .eq(StudentAnswer::getId,id)
        );

        if (!success) {
            log.error("更新其他学生评分失败，ID: {}", id);
            return Result.error("更新失败");
        }
        log.info("更新模型评分成功，ID: {}", id);
        return Result.success();
    }
    /**
     * 根据用户ID更新问题点赞数
     * @param isLike true=点赞，false=取消点赞
     * @return 操作结果
     */

    @PutMapping("/updateLike")
    public Result<?> updateStudentAnswerLike(
            @RequestParam Long answerId,
            @RequestParam boolean isLike) {
        studentAnswerService.updateLikeNumber(answerId, isLike);
        return Result.success();
    }

}
