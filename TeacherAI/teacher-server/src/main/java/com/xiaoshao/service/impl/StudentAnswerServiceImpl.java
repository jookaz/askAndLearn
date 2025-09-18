package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaoshao.entity.Question;
import com.xiaoshao.entity.StudentAnswer;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.mapper.StudentAnswerMapper;
import com.xiaoshao.service.IStudentAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-04
 */
@Service
public class StudentAnswerServiceImpl extends ServiceImpl<StudentAnswerMapper, StudentAnswer> implements IStudentAnswerService {
    @Override
    public Long createStudentAnswer(Long questionId, Long studentId, String answerContent, Integer systemScore) {
        // 1. 构造实体对象（使用链式赋值）
        StudentAnswer studentAnswer = new StudentAnswer()
                .setQuestionId(questionId)
                .setStudentId(studentId)
                .setAnswerContent(answerContent)
                .setSystemScore(systemScore)
                .setSubmitTime(LocalDateTime.now());

        // 2. 调用父类方法保存
        boolean success = save(studentAnswer);
        if (!success) {
            throw new BusinessException("学生回答记录创建失败");
        }

        return studentAnswer.getId();
    }

    @Override
    public String getStudentAnswerAnswerContentById(Long id) {
        // 直接调用父类getById方法（自动处理null值）
        StudentAnswer record = getById(id);
        return record != null ? record.getAnswerContent() : null;
    }
    @Override
//    根据id查询StudentAnswer
    public StudentAnswer getStudentAnswerById(Long questionId) {
        // 根据questionId查询
        LambdaQueryWrapper<StudentAnswer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentAnswer::getQuestionId, questionId);
        return getOne(queryWrapper);
    }
    /**
     * 查询所有 question_id 字段的值（允许重复）
     */
    @Override
    public List<Long> getQuestionIdList() {
        // 构造查询条件
        QueryWrapper<StudentAnswer> queryWrapper = new QueryWrapper<StudentAnswer>()
                .select("question_id");  // 直接写字段名

        // 查询并提取ID列表
        return listMaps(queryWrapper).stream()
                .map(map -> (Long) map.get("question_id"))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLikeNumber(Long answerId, boolean isLike) {
        // 方案1：使用MP的UpdateWrapper（原子操作）
        UpdateWrapper<StudentAnswer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", answerId)
                .setSql(isLike ? "like_number = like_number + 1" : "like_number = like_number - 1");

        if (!update(updateWrapper)) {
            throw new RuntimeException("更新点赞数失败");
        }
    }


}
