package com.xiaoshao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.dto.PromptTemplateDTO;
import com.xiaoshao.entity.PromptTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.vo.PromptTemplateVO;

import java.util.List;

/**
 * <p>
 * 提示词模板表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-25
 */
public interface IPromptTemplateService extends IService<PromptTemplate> {
    // 新增
    boolean savePrompt(PromptTemplateDTO dto);

    // 修改
    boolean updatePrompt(PromptTemplateDTO dto);

    // 删除
    boolean deleteByIds(List<Long> ids);

    // 分页查询
    Page<PromptTemplateVO> pageList(int pageNum, int pageSize);

    // ID查询
    PromptTemplateVO getDetailById(Long id);
//    根据名字查询
    PromptTemplateVO getDetailByName(String name);

}
