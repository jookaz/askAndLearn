package com.xiaoshao.mapper;

import com.xiaoshao.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoshao.vo.QuestionVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-27
 */
public interface QuestionMapper extends BaseMapper<Question> {
    // 自定义联合查询方法
    @Select("SELECT q.id, q.question_content, q.create_time, sa.answer_content, sa.id as answer_id " +
            "FROM question q LEFT JOIN student_answer sa ON q.id = sa.question_id " +
            "WHERE q.id = #{questionId}")
    QuestionVO selectQuestionWithAnswer(@Param("questionId") Long questionId);
}

