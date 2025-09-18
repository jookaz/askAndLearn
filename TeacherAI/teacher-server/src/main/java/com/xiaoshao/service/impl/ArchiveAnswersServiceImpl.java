package com.xiaoshao.service.impl;

import com.xiaoshao.dto.ArchiveAnswersDTO;
import com.xiaoshao.entity.ArchiveAnswers;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.mapper.ArchiveAnswersMapper;
import com.xiaoshao.service.IArchiveAnswersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 答案归档表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-19
 */
@Service
public class ArchiveAnswersServiceImpl extends ServiceImpl<ArchiveAnswersMapper, ArchiveAnswers> implements IArchiveAnswersService {
    @Override
    public Long createAnswer(ArchiveAnswersDTO dto) {
        // 1. DTO转Entity
        ArchiveAnswers entity = new ArchiveAnswers()
                .setContent(dto.getContent())
                .setQuestionId(dto.getQuestionId());

        // 3. 保存记录
        if (!save(entity)) {
            throw new BusinessException("答案创建失败");
        }

        // 4. 返回主键ID
        return entity.getId();
    }

}
