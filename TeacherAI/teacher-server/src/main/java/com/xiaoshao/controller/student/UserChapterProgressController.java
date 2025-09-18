package com.xiaoshao.controller.student;


import com.xiaoshao.dto.UserChapterProgressDTO;
import com.xiaoshao.entity.UserChapterProgress;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IUserChapterProgressService;
import com.xiaoshao.vo.UserChapterProgressVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-05
 */
@Slf4j
@Tag(name = "UserChapterProgressController", description = "用户章节进度管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("student/user-chapter-progress")
public class UserChapterProgressController {

    private final IUserChapterProgressService userChapterProgressService;

    /**
     * 根据用户ID、课程ID、章节ID获取进度记录（或创建新记录）
     */
    @GetMapping("/get-or-create")
    public Result<UserChapterProgressVO> getOrCreateProgress(
            UserChapterProgressDTO dto
    ) {
        UserChapterProgressVO progress = userChapterProgressService.getOrCreateProgress(dto.getUserId(), dto.getCourseId(), dto.getChapterId());
        return Result.success(progress);
    }
   /**
     * 获取所有进度记录，根据用户id以及课程id
     */
    @GetMapping("/list-by-user")
    public Result<List<UserChapterProgressVO>> listByUser(UserChapterProgressDTO dto) {
        return Result.success(userChapterProgressService.listProgressByUserIdAndCourseId(dto.getUserId(),dto.getCourseId()));
    }
    /**
     * 更新用户进度状态
     */
    @PostMapping("/update")
    public Result<?> updateProgressStatus(
            @RequestBody UserChapterProgressDTO dto // 1: 提出问题, 2: 模型回答, 3: 用户回答
    ) {
        userChapterProgressService.updateProgressStatus(dto.getUserId(), dto.getCourseId(), dto.getChapterId(), dto.getStep());
        return Result.success();
    }
}

