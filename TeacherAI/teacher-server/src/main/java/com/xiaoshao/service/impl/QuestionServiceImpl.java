package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.context.BaseContext;
import com.xiaoshao.dto.CourseDTO;
import com.xiaoshao.dto.QuestionDTO;
import com.xiaoshao.entity.*;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.mapper.QuestionMapper;
import com.xiaoshao.mapper.StudentAnswerAllMapper;
import com.xiaoshao.mapper.StudentAnswerMapper;
import com.xiaoshao.mapper.StudentImproveAnswerMapper;
import com.xiaoshao.result.PageResult;
import com.xiaoshao.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoshao.utils.BeanCopyUtil;
import com.xiaoshao.vo.CourseVO;
import com.xiaoshao.vo.QuestionVO;
import com.xiaoshao.vo.StudentAnswerAllVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-27
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    @Autowired
    private IStudentAnswerService studentAnswerService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IStudentImproveAnswerService studentImproveAnswerMapper;
    @Autowired
    private IStudentAnswerAllService studentAnswerAllMapper;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private IChapterService chapterService;

    @Override
    public QuestionVO getQuestionByParams(QuestionDTO dto) {
        // 1. 构建查询条件
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dto.getCourseId() != null, Question::getCourseId, dto.getCourseId())
                .eq(dto.getChapterId() != null, Question::getChapterId, dto.getChapterId())
                .eq(dto.getStudentId() != null, Question::getStudentId, dto.getStudentId());

        // 2. 查询单条记录
        Question question = getOne(queryWrapper);

        // 3. 转换为 VO 并返回
        if (question != null) {
            QuestionVO vo = new QuestionVO();
            vo.setId(question.getId());
            vo.setQuestionContent(question.getQuestionContent());
            return vo;
        }

        return null; // 或抛出异常
    }
    @Override
//    TODO 其实这里有一个可以改进的方面就是是否要分页查询
//    TODO 忘了一件事情这里需要修改，这里需要修改的地方就是和当前用户不一样
    public List<QuestionVO> listQuestionsByCourseAndChapter(Long courseId, Long chapterId) {
        // 1. 构建查询条件（仅包含courseId和chapterId）
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getCourseId, courseId)
                .eq(Question::getChapterId, chapterId)
                .ne(Question::getStudentId, BaseContext.getCurrentId());

        // 2. 查询列表
        List<Question> questions = list(queryWrapper);
//        如果问题还没有被用户进行回答，那么就不会被显示：被用户回答就是在student_answer表中有记录
//        如果问题已经被被其他同学回答就不会被显示
//        使用stream()进行过滤
        questions = questions.stream()
                .filter(question -> studentAnswerService.getQuestionIdList().contains(question.getId()))
                .filter(question -> question.getIfSelect() != 1)
                .collect(Collectors.toList());

        // 3. 转换为VO列表
        return BeanCopyUtil.copyListProperties(questions, QuestionVO::new);
    }
//    设计一个只根据课程以及章节id进行查询问题列表的方法
    @Override
    public List<QuestionVO> listAllQuestionsByCourseAndChapter(Long courseId, Long chapterId) {
    // 1. 构建查询条件
    LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Question::getCourseId, courseId)
            .eq(Question::getChapterId, chapterId);

    // 2. 获取问题列表
    List<Question> questions = list(queryWrapper);

    // 3. 过滤有学生回答的问题
    questions = questions.stream()
            .filter(question -> studentAnswerService.getQuestionIdList().contains(question.getId()))
            .toList();

    // 4. 快速返回空列表（如果questions为空）
    if (questions.isEmpty()) {
        return Collections.emptyList(); // 提前终止，避免后续无效查询
    }

    // 5. 获取学生ID集合（增加非空过滤）
    Set<Long> studentIds = questions.stream()
            .map(Question::getStudentId)
            .filter(Objects::nonNull) // 过滤掉null的studentId
            .collect(Collectors.toSet());

    // 6. 处理可能的学生ID空集合问题
    Map<Long, Student> studentMap;
    if (!studentIds.isEmpty()) {
        // 只有当studentIds非空时才查询数据库
        studentMap = studentService.listByIds(studentIds).stream()
                .collect(Collectors.toMap(Student::getId, student -> student));
    } else {
        // 创建空Map避免NPE
        studentMap = Collections.emptyMap();
    }

    // 7. 转换为VO列表
    return questions.stream()
            .map(question -> {
                QuestionVO vo = new QuestionVO();
                BeanUtils.copyProperties(question, vo);

                // 安全获取学生信息
                if (question.getStudentId() != null) {
                    Student student = studentMap.get(question.getStudentId());
                    if (student != null) {
                        vo.setStudentName(student.getStudentName());
                        vo.setClasses(student.getClasses());
                    }
                }
                return vo;
            })
            .collect(Collectors.toList());
}
    @Override
    public QuestionVO getQuestionWithAnswer(Long questionId) {
        return baseMapper.selectQuestionWithAnswer(questionId);
    }
    @Override
    public QuestionVO getQuestionDetailWithAnswers(Long questionId) {
        // 1. 查询问题基础信息
        Question question = getById(questionId);
        if (question == null) {
            return null;
        }

        // 2. 构建VO对象
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);

        // 3. 设置提问学生信息（增加null检查）
        if (question.getStudentId() != null) {
            Student student = studentService.getById(question.getStudentId());
            if (student != null) {
                vo.setStudentId(student.getId());
                vo.setStudentName(student.getStudentName());
                vo.setClasses(student.getClasses());
            }
        }

        // 4. 查询student_answer（单条）
        LambdaQueryWrapper<StudentAnswer> saWrapper = new LambdaQueryWrapper<>();
        saWrapper.eq(StudentAnswer::getQuestionId, questionId);
        StudentAnswer studentAnswer = studentAnswerService.getOne(saWrapper);
        if (studentAnswer != null && studentAnswer.getStudentId() != null) {
            Student student1 = studentService.getById(studentAnswer.getStudentId());
            if (student1 != null) {
                vo.setAnswerId(studentAnswer.getId());
                vo.setAnswerContent(studentAnswer.getAnswerContent());
                vo.setAnswerLikeNumber(studentAnswer.getLikeNumber());
                vo.setAnswerSubmitTime(studentAnswer.getSubmitTime());
                vo.setAnswerStudentId(student1.getId());
                vo.setAnswerStudentName(student1.getStudentName());
                vo.setAnswerStudentClasses(student1.getClasses());
            }
        }

        // 5. 查询student_improve_answer（单条）
        LambdaQueryWrapper<StudentImproveAnswer> siaWrapper = new LambdaQueryWrapper<>();
        siaWrapper.eq(StudentImproveAnswer::getQuestionId, questionId);
        StudentImproveAnswer improveAnswer = studentImproveAnswerMapper.getOne(siaWrapper);
        if (improveAnswer != null && improveAnswer.getStudentId() != null) {
            Student student2 = studentService.getById(improveAnswer.getStudentId());
            if (student2 != null) {
                vo.setImproveAnswerId(improveAnswer.getId());
                vo.setImproveAnswerContent(improveAnswer.getAnswerContent());
                vo.setImproveAnswerLikeNumber(improveAnswer.getLikeNumber());
                vo.setImproveAnswerSubmitTime(improveAnswer.getCreateTime());
                vo.setImproveAnswerStudentId(student2.getId());
                vo.setImproveAnswerStudentName(student2.getStudentName());
                vo.setImproveAnswerStudentClasses(student2.getClasses());
            }
        }

        // 6. 安全查询student_all_answer（修复重点）
        List<StudentAnswerAllVO> allAnswersVO = Collections.emptyList(); // 默认空列表
        LambdaQueryWrapper<StudentAnswerAll> allWrapper = new LambdaQueryWrapper<>();
        allWrapper.eq(StudentAnswerAll::getQuestionId, questionId)
                .orderByDesc(StudentAnswerAll::getSubmitTime);

        List<StudentAnswerAll> allAnswers = studentAnswerAllMapper.list(allWrapper);
        if (!allAnswers.isEmpty()) {
            // 6.1 获取有效学生ID（过滤null）
            Set<Long> studentIds = allAnswers.stream()
                    .map(StudentAnswerAll::getStudentId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // 6.2 批量查询学生信息（仅当有有效ID时）
            Map<Long, Student> studentMap = studentIds.isEmpty()
                    ? Collections.emptyMap()
                    : studentService.listByIds(studentIds).stream()
                    .collect(Collectors.toMap(Student::getId, Function.identity()));

            // 6.3 转换为VO
            allAnswersVO = allAnswers.stream()
                    .map(answer -> {
                        StudentAnswerAllVO allVO = new StudentAnswerAllVO();
                        BeanUtils.copyProperties(answer, allVO);

                        // 安全设置学生信息
                        if (answer.getStudentId() != null) {
                            Student student = studentMap.get(answer.getStudentId());
                            if (student != null) {
                                allVO.setStudentName(student.getStudentName());
                                allVO.setClasses(student.getClasses());
                            }
                        }
                        // 设置默认值
                        if (allVO.getStudentName() == null) {
                            allVO.setStudentName("未知学生");
                            allVO.setClasses("未分配班级");
                        }
                        return allVO;
                    })
                    .collect(Collectors.toList());
        }
        vo.setStudentAnswerAllVOList(allAnswersVO);

        return vo;
    }
    @Override
    public boolean updateSelectStatus(Long questionId) {
        // 方案1：使用MP的UpdateWrapper（推荐）
        UpdateWrapper<Question> updateWrapper = new UpdateWrapper<Question>()
                .eq("id", questionId)
                .set("if_select", 1);  // 直接更新指定字段

        return update(updateWrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLikeNumber(Long questionId, boolean isLike) {
        // 方案1：使用MP的UpdateWrapper（原子操作）
        UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", questionId)
                .setSql(isLike ? "like_number = like_number + 1" : "like_number = like_number - 1");

        if (!update(updateWrapper)) {
            throw new RuntimeException("更新点赞数失败");
        }
    }

    @Override
    public PageResult<QuestionVO> queryQuestionsPage(QuestionDTO dto) {
        Page<Question> page = dto.toMpPage(OrderItem.desc("update_time"));

        // 2. 构建动态查询条件
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
//        当前用户所创建的课程id
        List<Long> userCourseIds = courseService.getCourseIds();
        if(userCourseIds.isEmpty()){
            return null;
        }
        wrapper.in(Question::getCourseId,  userCourseIds);
        wrapper.eq(dto.getCourseId() != null, Question::getCourseId, dto.getCourseId())
                .eq(dto.getChapterId() != null, Question::getChapterId, dto.getChapterId())
                .eq(StringUtils.isNotBlank(dto.getCognitiveLevel()),
                        Question::getCognitiveLevel, dto.getCognitiveLevel())
                .like(StringUtils.isNotBlank(dto.getQuestionContent()),
                        Question::getQuestionContent, dto.getQuestionContent());
//        开始分页查询
        page(page, wrapper);

        PageResult<QuestionVO> pageResult = PageResult.of(page, QuestionVO::new);
        if (!pageResult.getRecords().isEmpty()) {
//            这里没有考虑章节为空的情况根据逻辑来看问题的章节不可能为空
            // 1. 提前收集所有ID
            Set<Long> courseIds = pageResult.getRecords().stream()
                    .map(QuestionVO::getCourseId)
                    .collect(Collectors.toSet());
            Set<Long> chapterIds = pageResult.getRecords().stream()
                    .map(QuestionVO::getChapterId)
                    .collect(Collectors.toSet());

            // 2. 批量查询（减少数据库访问）
            Map<Long, String> courseNameMap = courseService.listByIds(courseIds).stream()
                    .collect(Collectors.toMap(Course::getId, Course::getCourseName));
            Map<Long, String> chapterNameMap = chapterService.listByIds(chapterIds).stream()
                    .collect(Collectors.toMap(Chapter::getId, Chapter::getChapterName));

            // 3. 直接映射
            pageResult.getRecords().forEach(vo -> {
                vo.setCourseName(courseNameMap.get(vo.getCourseId()));
                vo.setChapterName(chapterNameMap.get(vo.getChapterId()));
            });
        }
        return pageResult;
    }
    @Override
    public Long createQuestion(
            String questionContent,
            Long courseId,
            Long chapterId,
            Long studentId,
            String cognitiveLevel
    ) {
        // 1. 构造实体对象（利用链式赋值）
        Question question = new Question()
                .setQuestionContent(questionContent)
                .setCourseId(courseId)
                .setChapterId(chapterId)
                .setStudentId(studentId)
                .setCognitiveLevel(cognitiveLevel);

        // 2. 调用父类save方法（自动处理主键生成和事务）
        boolean success = save(question);
        if (!success) {
            throw new BusinessException("问题创建失败");
        }

        return question.getId();
    }
}
