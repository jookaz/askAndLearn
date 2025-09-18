package com.xiaoshao.service;

import com.xiaoshao.dto.ArchiveQuestionsDTO;
import com.xiaoshao.dto.CourseDTO;
import com.xiaoshao.entity.ArchiveQuestions;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.result.PageResult;
import com.xiaoshao.vo.ArchiveQuestionsVO;
import com.xiaoshao.vo.CourseVO;

/**
 * <p>
 * 问题归档表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-19
 */
public interface IArchiveQuestionsService extends IService<ArchiveQuestions> {
    Long createQuestion(ArchiveQuestionsDTO dto);
    PageResult<ArchiveQuestionsVO> page(ArchiveQuestionsDTO dto);
}
