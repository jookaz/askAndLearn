package com.xiaoshao.controller.teacher;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.dto.AiModelConfigDTO;
import com.xiaoshao.entity.AiModelConfig;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IAiModelConfigService;
import com.xiaoshao.vo.AiModelConfigVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * AI模型配置表 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-17
 */
@Tag(name = "ModelController", description = "模型管理类")
@Slf4j
@RestController("teacherModelController")
@RequestMapping("/teacher/model")
@RequiredArgsConstructor
public class ModelController {
    private final IAiModelConfigService modelConfigService;

    @PostMapping
    public Result<Void> createModel(@RequestBody AiModelConfigDTO dto) {
        return modelConfigService.createModel(dto) ?
                Result.success() : Result.error("创建失败");
    }

    @GetMapping
    public Result<IPage<AiModelConfigVO>> listModels(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(
                modelConfigService.listUserModels(new Page<AiModelConfig>(pageNum, pageSize))
        );
    }

    @DeleteMapping
    public Result<Void> deleteModel(@RequestParam("ids")List<Long> ids) {
        return modelConfigService.deleteModelConfigs(ids) ?
                Result.success() : Result.error("删除失败或无权操作");
    }

    @GetMapping("/{id}")
    public Result<AiModelConfigVO> getModel(@PathVariable Integer id) {
        return Result.success(modelConfigService.getModelById(id));
    }
    // 修改模型（新增接口）
    @PutMapping
    public Result<Void> updateModel(
                                    @RequestBody AiModelConfigDTO dto) {
        return modelConfigService.updateModel(dto) ?
                Result.success() : Result.error("更新失败");
    }
}
