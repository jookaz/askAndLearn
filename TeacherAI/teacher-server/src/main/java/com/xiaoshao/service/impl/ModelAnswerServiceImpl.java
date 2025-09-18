package com.xiaoshao.service.impl;

import com.xiaoshao.entity.ModelAnswer;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.mapper.ModelAnswerMapper;
import com.xiaoshao.service.IModelAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-02
 */
@Service
public class ModelAnswerServiceImpl extends ServiceImpl<ModelAnswerMapper, ModelAnswer> implements IModelAnswerService {
    @Override
    public Long createModelAnswer(Long questionId, String answerContent, String modelUsed) {
        // 1. 构造实体对象（利用MyBatis-Plus的自动填充功能）
        ModelAnswer modelAnswer = new ModelAnswer()
                .setQuestionId(questionId)
                .setAnswerContent(answerContent)
                .setModelUsed(modelUsed)
                .setStudentScore(0); // 默认值

        // 2. 调用父类save方法（无需手动注入Mapper）
        boolean success = save(modelAnswer);
        if (!success) {
            throw new BusinessException("创建模型回答记录失败");
        }

        return modelAnswer.getId();
    }

    @Override
    public String getModelAnswerById(Long id) {
        // 1. 调用父类getById方法（自动处理null值）
        ModelAnswer record = getById(id);
        if (record == null) {
            throw new BusinessException("未找到对应记录");
        }
        return record.getAnswerContent();
    }

}
