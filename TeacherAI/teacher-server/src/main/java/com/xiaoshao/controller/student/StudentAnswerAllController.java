package com.xiaoshao.controller.student;


import com.xiaoshao.dto.StudentAnswerAllDTO;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IStudentAnswerAllService;
import com.xiaoshao.service.IStudentImproveAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("student/studentAnswerAll")
public class StudentAnswerAllController {
    private final IStudentAnswerAllService studentAnswerAllService;
    /**
     * 根据用户ID更新问题点赞数
     * @param isLike true=点赞，false=取消点赞
     * @return 操作结果
     */
    @PutMapping("/updateLike")
    public Result<?> updateStudentAnswerAllLike(
            @RequestParam Long studentAnswerAllId,
            @RequestParam boolean isLike) {
        studentAnswerAllService.updateLikeNumber(studentAnswerAllId, isLike);
        return Result.success();
    }
//    TODO注意这里的接口不是通过前端传递用户id而是在后端获取
    /**
     * 创建学生答案记录
     * @param dto 包含问题ID和答案内容
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result<Long> createStudentAnswerAll(@RequestBody StudentAnswerAllDTO dto) {
        Long recordId = studentAnswerAllService.createAnswerRecord(dto);
        return Result.success(recordId);
    }
}
