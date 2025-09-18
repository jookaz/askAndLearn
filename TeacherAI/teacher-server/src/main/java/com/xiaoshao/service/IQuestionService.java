package com.xiaoshao.service;

import com.xiaoshao.dto.CourseDTO;
import com.xiaoshao.dto.QuestionDTO;
import com.xiaoshao.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.result.PageResult;
import com.xiaoshao.vo.CourseVO;
import com.xiaoshao.vo.QuestionVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-27
 */
public interface IQuestionService extends IService<Question> {
    /**
     * 根据条件查询单条问题记录
     */
    QuestionVO getQuestionByParams(QuestionDTO dto);
    /**
     * 根据条件查询多条问题记录
     */
    List<QuestionVO> listQuestionsByCourseAndChapter(Long courseId, Long chapterId);
    /**
     *根据章节以及课程id查询问题记录
     */
    List<QuestionVO> listAllQuestionsByCourseAndChapter(Long courseId, Long chapterId);
    /**
     * 根据id查询问题记录以及对应学生答案
     */
    QuestionVO getQuestionWithAnswer(Long questionId);
    /**
     * 根据id查询问题记录以及所有答案
     */
     QuestionVO getQuestionDetailWithAnswers(Long questionId);
    /**
     * 更新问题的选中状态
     * @param questionId 问题ID
     * @return 是否成功
     */
    boolean updateSelectStatus(Long questionId);

    /**
     * 更新问题点赞数

     * @param questionId 问题ID
     * @param isLike 是否点赞
     */
    void updateLikeNumber(Long questionId, boolean isLike);

    /**
     * 教师端分页查询
     */
    PageResult<QuestionVO> queryQuestionsPage(QuestionDTO questionDTO);
    /**
     * 创建新问题
     * @param questionContent 问题内容
     * @param courseId 关联课程ID
     * @param chapterId 关联章节ID
     * @param studentId 提问学生ID
     * @param cognitiveLevel 认知层次（如记忆、理解、应用等）
     * @return 新问题的ID
     * @throws BusinessException 若创建失败则抛出业务异常
     */
    Long createQuestion(
            String questionContent,
            Long courseId,
            Long chapterId,
            Long studentId,
            String cognitiveLevel
    ) throws BusinessException;
}
