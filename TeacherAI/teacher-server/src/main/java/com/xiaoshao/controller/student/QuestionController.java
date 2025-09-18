package com.xiaoshao.controller.student;


import com.xiaoshao.dto.QuestionDTO;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IQuestionService;
import com.xiaoshao.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-27
 */
@RestController("studentQuestionController")
@RequestMapping("student/question")
@RequiredArgsConstructor
public class QuestionController {

    private  final IQuestionService questionService;

    /**
     * 根据条件查询单条问题记录
     */
    @GetMapping("/get-by-id")
    public Result<QuestionVO> getQuestionByParams(QuestionDTO dto,@RequestParam("userId") Long userId) {
        try {
//            TODO这是一个很重要的错误，前端传递的userId，但是我们的数据库是student_id
            dto.setStudentId(userId);
            QuestionVO vo = questionService.getQuestionByParams(dto);
            return Result.success(vo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    @GetMapping("/list")
    public Result<List<QuestionVO>> listQuestions(
           QuestionDTO dto
    ) {
        List<QuestionVO> questions = questionService.listQuestionsByCourseAndChapter(dto.getCourseId(), dto.getChapterId());
        return Result.success(questions);
    }

    /**
     * 根据条件查询所有的问题记录
     * @param dto
     * @return
     */
    @GetMapping("/listAll")
    public Result<List<QuestionVO>> listAllQuestions(QuestionDTO dto) {
        List<QuestionVO> questions = questionService.listAllQuestionsByCourseAndChapter(dto.getCourseId(), dto.getChapterId());
        return Result.success(questions);
    }
    @GetMapping("/detail")
    public Result<QuestionVO> getQuestionDetail(@RequestParam Long questionId) {
        QuestionVO vo = questionService.getQuestionWithAnswer(questionId);
        return vo != null ? Result.success(vo) : Result.error("问题不存在");
    }
    /**
     * 获取问题的多个答案信息
     */
    @GetMapping("/allAnswer")
    public Result<QuestionVO> getAllAnswer(@RequestParam Long questionId) {
        QuestionVO vo = questionService.getQuestionDetailWithAnswers(questionId);
        return vo != null ? Result.success(vo) : Result.error("问题不存在");
    }
//    修改问题表的if_select字段的值
    /**
     * 根据问题ID设置选中状态
     * @param questionId 问题ID
     * @return 操作结果
     */
    @PutMapping("/updateIfSelect")
    public Result<Void> selectQuestion(@RequestParam Long questionId) {
        questionService.updateSelectStatus(questionId);
        return Result.success();
    }

    /**
     * 根据用户ID更新问题点赞数
     * @param questionId 问题ID
     * @param isLike true=点赞，false=取消点赞
     * @return 操作结果
     */
    @PutMapping("/updateLike")
    public Result<?> updateQuestionLike(
            @RequestParam Long questionId,
            @RequestParam boolean isLike) {
        questionService.updateLikeNumber(questionId, isLike);
        return Result.success();
    }

}
