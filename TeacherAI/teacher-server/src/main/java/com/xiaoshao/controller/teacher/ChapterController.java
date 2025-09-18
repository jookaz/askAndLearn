package com.xiaoshao.controller.teacher;

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
import java.util.Map;

/**
 * <p>
 * 章节表 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-21
 */
@Tag(name = "章节管理", description = "章节管理")
@RestController("teacherChapterController")
@Slf4j
@RequestMapping("/teacher/chapters")
public class ChapterController {
    @Autowired
    private IChapterService chapterService;

    /**
     * 创建章节
     * @param chapterDTO 章节信息
     * @return 操作结果
     */
    @Operation(summary = "创建章节")
    @PostMapping
    public Result<?> createChapter(@RequestBody ChapterDTO chapterDTO) {
        chapterService.createChapter(chapterDTO);
        return Result.success();
    }

    /**
     * 更新章节
     * @param chapterDTO 章节信息
     * @return 操作结果
     */
    @Operation(summary = "更新章节")
    @PutMapping
    public Result<?> updateChapter(@RequestBody ChapterDTO chapterDTO) {
        chapterService.updateChapter(chapterDTO);
        return Result.success();
    }

    /**
     * 删除章节
     * @param chapterId 章节ID
     * @return 操作结果
     */
    @Operation(summary = "删除章节")
    @DeleteMapping("/{chapterId}")
    public Result<?> deleteChapter(@PathVariable Long chapterId) {
        chapterService.deleteChapter(chapterId);
        return Result.success();
    }

    /**
     * 获取章节列表
     * @return 章节列表
     */
    @Operation(summary = "获取章节列表")
    @GetMapping
    public Result<?> getChapterList() {
        List<ChapterVO> list = chapterService.getChapterList();
        return Result.success(list);
    }
    /**
     * 获取章节名称列表
     */
    @Operation(summary = "获取章节名称列表")
    @GetMapping("/names")
    public Result<List<Map<String,  Long>>> getChapterNames(Long courseId) {
        return Result.success(chapterService.getChapterNames(courseId));
    }

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

    /**
     * 分页查询章节
     * @param chapterDTO 分页条件（如 pageNo、pageSize）
     * @return 分页结果
     */
    @Operation(summary = "分页查询章节")
    @GetMapping("/page")
    public Result<?> page(@RequestBody ChapterDTO chapterDTO) {
        return Result.success(chapterService.queryChaptersPage(chapterDTO));
    }
}
