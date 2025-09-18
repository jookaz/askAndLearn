package com.xiaoshao.controller.student;

import com.xiaoshao.dto.ChapterDTO;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IChapterService;
import com.xiaoshao.vo.ChapterVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 章节表 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-21
 */
@Tag(name = "章节管理", description = "章节管理")
@RestController("studentChapterController")
@Slf4j
@RequestMapping("/student/chapters")
public class ChapterController {
    @Autowired
    private IChapterService chapterService;

    /**
     * 根据章节ID获取章节详情
     * @param chapterId 章节ID
     * @return 章节详情
     */
    @Operation(summary = "根据章节ID获取章节详情")
    @GetMapping("/{chapterId}")
    public Result<?> getChapterDetail(@PathVariable Long chapterId) {
        ChapterVO chapterVO = chapterService.getChapterDetail(chapterId);
        return Result.success(chapterVO);
    }


}
