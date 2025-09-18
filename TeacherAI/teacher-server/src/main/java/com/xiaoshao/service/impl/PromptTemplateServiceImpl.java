package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.context.BaseContext;
import com.xiaoshao.dto.PromptTemplateDTO;
import com.xiaoshao.entity.PromptTemplate;
import com.xiaoshao.mapper.PromptTemplateMapper;
import com.xiaoshao.service.IPromptTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoshao.vo.PromptTemplateVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 提示词模板表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-25
 */
@Service
public class PromptTemplateServiceImpl extends ServiceImpl<PromptTemplateMapper, PromptTemplate> implements IPromptTemplateService {
    @Override
    public boolean savePrompt(PromptTemplateDTO dto) {
        PromptTemplate entity = new PromptTemplate()
                .setPromptName(dto.getPromptName())
                .setPromptContent(dto.getPromptContent());
        return this.save(entity);
    }

    @Override
    public boolean updatePrompt(PromptTemplateDTO dto) {
        return this.lambdaUpdate()
                .eq(PromptTemplate::getId, dto.getId())
                .set(PromptTemplate::getPromptName, dto.getPromptName())
                .set(PromptTemplate::getPromptContent, dto.getPromptContent())
                .update();
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    @Override
    public Page<PromptTemplateVO> pageList(int pageNum, int pageSize) {
        return (Page<PromptTemplateVO>) this.lambdaQuery()
                .page(new Page<>(pageNum, pageSize))
                .convert(this::toVO);
    }

    @Override
    public PromptTemplateVO getDetailById(Long id) {
        PromptTemplate entity = this.getById(id);
        return toVO(entity);
    }
    @Override
    public PromptTemplateVO getDetailByName(String name) {
        PromptTemplate entity = this.lambdaQuery()
                .eq(PromptTemplate::getPromptName, name)
                .one();
        return toVO(entity);
    }

    private PromptTemplateVO toVO(PromptTemplate entity) {
        if (entity == null) return null;
        return new PromptTemplateVO()
                .setId(entity.getId())
                .setPromptName(entity.getPromptName())
                .setPromptContent(entity.getPromptContent())
                .setUpdateUser(entity.getUpdateUser());
    }

}
