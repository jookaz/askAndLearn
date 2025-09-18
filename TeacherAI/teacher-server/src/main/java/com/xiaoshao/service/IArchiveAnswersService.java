package com.xiaoshao.service;

import com.xiaoshao.dto.ArchiveAnswersDTO;
import com.xiaoshao.entity.ArchiveAnswers;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 答案归档表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-19
 */
public interface IArchiveAnswersService extends IService<ArchiveAnswers> {
    /**
     * 创建归档答案
     * @param dto 传输对象
     * @return 创建记录的主键ID
     */
    Long createAnswer(ArchiveAnswersDTO dto);

}
