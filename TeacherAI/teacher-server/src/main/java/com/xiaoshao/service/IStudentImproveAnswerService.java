package com.xiaoshao.service;

import com.xiaoshao.entity.StudentImproveAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.exception.BusinessException;

/**
 * <p>
 * 学生提升回答记录 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-09
 */
public interface IStudentImproveAnswerService extends IService<StudentImproveAnswer> {
    /**
     * 创建学生改进答案记录
     * @param questionId 关联问题ID
     * @param studentId 学生ID
     * @param modelScore 模型评分
     * @param answerContent 改进后的答案内容
     * @return 新记录的主键ID
     * @throws BusinessException 创建失败时抛出
     */
    Long createStudentImproveAnswer(
            Long questionId,
            Long studentId,
            Integer modelScore,
            String answerContent
    ) throws BusinessException;
    /**
     * 更新问题点赞数
     * @param isLike 是否点赞
     */
    void updateLikeNumber(Long answerId, boolean isLike);

}
