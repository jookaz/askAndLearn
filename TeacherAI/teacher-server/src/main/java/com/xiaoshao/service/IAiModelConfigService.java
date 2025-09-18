package com.xiaoshao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.dto.AiModelConfigDTO;
import com.xiaoshao.entity.AiModelConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.vo.AiModelConfigVO;

import java.util.List;

/**
 * <p>
 * AI模型配置表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-17
 */
public interface IAiModelConfigService extends IService<AiModelConfig> {
    // 创建模型（自动设置createUser和systemFlag=0）
    boolean createModel(AiModelConfigDTO dto);
//    创建系统模型
    boolean createSystemModel(AiModelConfigDTO dto);

    // 分页查询用户模型（过滤系统模型）
    IPage<AiModelConfigVO> listUserModels(Page<AiModelConfig> page);
//    分页查询系统模型
    IPage<AiModelConfigVO> listSystemModels(Page<AiModelConfig> page);
    // 安全删除（限制只能删除自己的非系统模型）
    boolean deleteModelConfigs(List<Long> ids);


    // 根据ID查询模型（用于回显）
    AiModelConfigVO getModelById(Integer id);

    // 修改模型（新增）
    boolean updateModel(AiModelConfigDTO dto);

//
    boolean  setSystemModel(Integer modelId);

}
