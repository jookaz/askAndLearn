package com.xiaoshao.service;

import com.xiaoshao.entity.StudentAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.exception.BusinessException;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-04
 */
public interface IStudentAnswerService extends IService<StudentAnswer> {
    /**
     * 创建学生回答记录
     * @param questionId 关联问题ID
     * @param studentId 回答学生ID
     * @param answerContent 学生答案内容
     * @param systemScore 系统评分
     * @return 新记录的主键ID
     * @throws BusinessException 创建失败时抛出
     */
    Long createStudentAnswer(Long questionId, Long studentId, String answerContent, Integer systemScore)
            throws BusinessException;

    /**
     * 根据ID查询学生回答内容
     * @param id 记录主键ID
     * @return 回答内容（可能为null）
     */
    String getStudentAnswerAnswerContentById(Long id);
    /**
     * 查询所有 question_id 字段的值（允许重复）
     */
    List<Long> getQuestionIdList();
    /**
     * 更新问题点赞数
     * @param isLike 是否点赞
     */
    void updateLikeNumber(Long improveAnswerId, boolean isLike);
    /**
     * 根据id查询答案
     */
    StudentAnswer getStudentAnswerById(Long questionId);



}
