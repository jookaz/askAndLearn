package com.xiaoshao.service;

import com.xiaoshao.entity.ModelAnswer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-02
 */
public interface IModelAnswerService extends IService<ModelAnswer> {
    /**
     * 创建大模型回答记录
     * @param questionId 关联问题ID
     * @param answerContent 大模型回答内容
     * @param modelUsed 使用的模型名称（如qwen-turbo）
     * @return 新记录的主键ID
     */
    Long createModelAnswer(Long questionId, String answerContent, String modelUsed);

    /**
     * 通过ID查询大模型回答记录内容
     * @param id 记录主键ID
     * @return 回答内容
     */
    String getModelAnswerById(Long id);

}
