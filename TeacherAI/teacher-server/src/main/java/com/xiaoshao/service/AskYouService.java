package com.xiaoshao.service;

import com.xiaoshao.service.impl.AskYouServiceImpl;
import com.xiaoshao.vo.ModelQuestionResponseVO;
import com.xiaoshao.vo.ModelsAnswerResponseVO;
import com.xiaoshao.vo.StudentAnswerResponseVO;

import java.util.List;
import java.util.Map;

public interface AskYouService {
    ModelQuestionResponseVO systemChat(String prompt, Long userId, Long courseId, Long chapterId);
    StudentAnswerResponseVO systemChatAnswer(String prompt, Long userId, Long courseId, Long chapterId, String question, Long questionId);
    StudentAnswerResponseVO systemChatImproveAnswer(String prompt, Long userId, Long courseId, Long chapterId, String question, Long questionId);
    List<ModelsAnswerResponseVO> multiModelChat(List<String> modelNames, String question, Long userId, Long courseId, Long chapterId, Long questionId);
    List<String> getAvailableModels(Long courseId);
    AskYouServiceImpl.CourseChapterNames getNamesObject(Long courseId, Long chapterId);
}