package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.context.BaseContext;
import com.xiaoshao.dto.ArchiveAnswersDTO;
import com.xiaoshao.dto.ArchiveQuestionsDTO;
import com.xiaoshao.entity.ArchiveAnswers;
import com.xiaoshao.entity.ArchiveQuestions;
import com.xiaoshao.entity.Course;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.mapper.ArchiveQuestionsMapper;
import com.xiaoshao.result.PageResult;
import com.xiaoshao.service.IArchiveQuestionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoshao.vo.ArchiveQuestionsVO;
import com.xiaoshao.vo.CourseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 问题归档表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-19
 */
@Service
public class ArchiveQuestionsServiceImpl extends ServiceImpl<ArchiveQuestionsMapper, ArchiveQuestions> implements IArchiveQuestionsService {
    @Autowired
    private ArchiveAnswersServiceImpl archiveAnswersService;
    @Override
    public Long createQuestion(ArchiveQuestionsDTO dto) {
        // 1. DTO转Entity
        ArchiveQuestions entity = new ArchiveQuestions()
                .setContent(dto.getContent())
                .setCourseId(dto.getCourseId())
                .setChapterId(dto.getChapterId())
                .setOriginalQuestionId(dto.getOriginalQuestionId());

        // 3. 保存记录
        if (!save(entity)) {
            throw new BusinessException("答案创建失败");
        }

        // 4. 返回主键ID
        return entity.getId();
    }

    @Override
    public PageResult<ArchiveQuestionsVO> page(ArchiveQuestionsDTO dto) {
        // 1. 执行分页查询基础问题数据
        Page<ArchiveQuestions> page = dto.toMpPageDefaultSortByCreateTime();
        LambdaQueryWrapper<ArchiveQuestions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArchiveQuestions::getCourseId, dto.getCourseId())
                .eq(ArchiveQuestions::getChapterId, dto.getChapterId());
        page(page, queryWrapper);

        // 2. 如果没有数据直接返回空结果
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return PageResult.of(page, ArchiveQuestionsVO::new);
        }

        // 3. 获取所有问题ID集合
        List<Long> questionIds = page.getRecords().stream()
                .map(ArchiveQuestions::getId)
                .collect(Collectors.toList());

        // 4. 批量查询关联的答案数据（按问题ID分组）
        Map<Long, String> answerContentMap = archiveAnswersService.lambdaQuery()
                .in(ArchiveAnswers::getQuestionId, questionIds)
                .list()
                .stream()
                .collect(Collectors.toMap(
                        ArchiveAnswers::getQuestionId,
                        ArchiveAnswers::getContent,
                        (existing, replacement) -> existing // 如果有多个答案，保留第一个
                ));

        // 5. 构建VO列表并设置答案内容
        return PageResult.of(page, question -> {
            ArchiveQuestionsVO vo = new ArchiveQuestionsVO();
            BeanUtils.copyProperties(question, vo);
            // 设置关联的答案内容
            vo.setAnswerContent(answerContentMap.get(question.getId()));
            return vo;
        });
    }


}
