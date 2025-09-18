package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaoshao.entity.StudentAnswer;
import com.xiaoshao.entity.StudentImproveAnswer;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.mapper.StudentImproveAnswerMapper;
import com.xiaoshao.service.IStudentImproveAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 学生提升回答记录 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-09
 */
@Service
public class StudentImproveAnswerServiceImpl extends ServiceImpl<StudentImproveAnswerMapper, StudentImproveAnswer> implements IStudentImproveAnswerService {
    @Override
    public Long createStudentImproveAnswer(
            Long questionId,
            Long studentId,
            Integer modelScore,
            String answerContent
    ) {
        // 1. 构造实体对象（使用链式赋值）
        StudentImproveAnswer answer = new StudentImproveAnswer()
                .setQuestionId(questionId)
                .setStudentId(studentId)
                .setAnswerContent(answerContent)
                .setModelScore(modelScore)
                .setSelfScore(0) // 默认值
                .setCreateTime(LocalDateTime.now());

        // 2. 调用父类save方法（自动处理主键生成）
        if (!save(answer)) {
            throw new BusinessException("改进答案记录创建失败");
        }
        return answer.getId();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLikeNumber(Long improveAnswerId, boolean isLike) {
        // 方案1：使用MP的UpdateWrapper（原子操作）
        UpdateWrapper<StudentImproveAnswer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", improveAnswerId)
                .setSql(isLike ? "like_number = like_number + 1" : "like_number = like_number - 1");

        if (!update(updateWrapper)) {
            throw new RuntimeException("更新点赞数失败");
        }
    }
}
