package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.context.BaseContext;
import com.xiaoshao.dto.AiModelConfigDTO;
import com.xiaoshao.entity.AiModelConfig;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.mapper.AiModelConfigMapper;
import com.xiaoshao.service.IAiModelConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoshao.vo.AiModelConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * AI模型配置表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-17
 */
@Service
@RequiredArgsConstructor
public class AiModelConfigServiceImpl extends ServiceImpl<AiModelConfigMapper, AiModelConfig> implements IAiModelConfigService {
    private final AiModelConfigMapper modelConfigMapper;

    @Override
    public boolean createModel(AiModelConfigDTO dto) {
        AiModelConfig entity = new AiModelConfig();
        BeanUtils.copyProperties(dto, entity);
        entity.setSystemFlag(0); // 用户创建的模型标记为非系统模型
        return save(entity);
    }
    @Override
    public boolean createSystemModel(AiModelConfigDTO dto) {
        AiModelConfig entity = new AiModelConfig();
        BeanUtils.copyProperties(dto, entity);
        entity.setSystemFlag(0);
        return save(entity);
    }

    @Override
    public IPage<AiModelConfigVO> listUserModels(Page<AiModelConfig> page) {
        return modelConfigMapper.selectPage(page,
                new LambdaQueryWrapper<AiModelConfig>()
                        .eq(AiModelConfig::getCreateUser, BaseContext.getCurrentId())
        ).convert(this::convertToVO);
    }
    @Override
    public IPage<AiModelConfigVO> listSystemModels(Page<AiModelConfig> page) {
        return modelConfigMapper.selectPage(page,
                new LambdaQueryWrapper<AiModelConfig>()
                        .eq(AiModelConfig::getCreateUser, 0)
        ).convert(this::convertToVO);
    }

    @Override
    public boolean deleteModelConfigs(List<Long> ids){
        if (ids == null || ids.isEmpty()){
            return false;
        }
        return removeByIds(ids);
    }


    @Override
    public AiModelConfigVO getModelById(Integer id) {
        AiModelConfig entity = getOne(new LambdaQueryWrapper<AiModelConfig>()
                .eq(AiModelConfig::getId, id)
        );
        return convertToVO(entity);
    }
    // 修改模型（新增核心逻辑）
    @Override
    public boolean updateModel(AiModelConfigDTO dto) {
        // 1. 校验所有权
        if (!exists(new LambdaQueryWrapper<AiModelConfig>()
                .eq(AiModelConfig::getId, dto.getId())
        )) {
            throw new BusinessException("不存在该模型");
        }
        // 2. DTO转Entity
        AiModelConfig entity = new AiModelConfig();
        BeanUtils.copyProperties(dto, entity);

        // 3. 执行更新（MP的updateById会自动忽略null字段）
        return updateById(entity);
    }

    private AiModelConfigVO convertToVO(AiModelConfig entity) {
        if (entity == null) return null;
        AiModelConfigVO vo = new AiModelConfigVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
//    返回值为布尔类型，然后作用是设置为系统模型
    @Override
    public boolean setSystemModel(Integer modelId) {
        // 1. 校验是否存在
        if (!exists(new LambdaQueryWrapper<AiModelConfig>()
                .eq(AiModelConfig::getId, modelId)
        )) {
            throw new BusinessException("不存在该模型");
        }

        // 开启事务（假设你的MyBatis-Plus集成了事务管理，比如在Spring环境中利用@Transactional注解等）
        // 这里只是示意，具体事务管理方式根据你的项目配置来

        // 2. 先将所有模型的系统标记设置为0
        boolean updateAllToZeroSuccess = update(new LambdaUpdateWrapper<AiModelConfig>()
                .set(AiModelConfig::getSystemFlag, 0)
        );
        if (!updateAllToZeroSuccess) {
            // 可以根据实际情况决定是否抛出异常或者进行其他处理，比如记录日志
            // 这里简单示例抛出异常
            throw new RuntimeException("设置其他模型系统标记为0失败");
        }

        // 3. 再将指定模型的系统标记设置为1
        return update(new LambdaUpdateWrapper<AiModelConfig>()
                .eq(AiModelConfig::getId, modelId)
                .set(AiModelConfig::getSystemFlag, 1)
        );
    }
}
