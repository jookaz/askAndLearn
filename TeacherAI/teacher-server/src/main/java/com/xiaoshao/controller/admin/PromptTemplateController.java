package com.xiaoshao.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.dto.PromptTemplateDTO;
import com.xiaoshao.entity.PromptTemplate;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IPromptTemplateService;
import com.xiaoshao.vo.PromptTemplateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 提示词模板表 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-25
 */
@RestController
@RequestMapping("admin/prompt-template")
public class PromptTemplateController {
    @Autowired
    private IPromptTemplateService promptService;

    // 新增
    @PostMapping
    public Result<Void> add(@RequestBody PromptTemplateDTO dto) {
        return promptService.savePrompt(dto) ?
                Result.success() : Result.error("新增失败");
    }

    // 修改
    @PutMapping
    public Result<Void> update(@RequestBody PromptTemplateDTO dto) {
        return promptService.updatePrompt(dto) ?
                Result.success() : Result.error("更新失败");
    }

    // 删除
    @DeleteMapping
    public Result<Void> delete(@RequestParam("ids") List<Long> ids) {
        return promptService.deleteByIds(ids) ?
                Result.success() : Result.error("删除失败");
    }

    // 分页查询
    @GetMapping("/page")
    public Result<Page<PromptTemplateVO>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(promptService.pageList(pageNum, pageSize));
    }

    // 详情
    @GetMapping("/{id}")
    public Result<PromptTemplateVO> detail(@PathVariable Long id) {
        return Result.success(promptService.getDetailById(id));
    }

}
