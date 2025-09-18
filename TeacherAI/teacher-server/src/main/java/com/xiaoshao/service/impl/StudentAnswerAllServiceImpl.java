package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaoshao.context.BaseContext;
import com.xiaoshao.dto.StudentAnswerAllDTO;
import com.xiaoshao.entity.StudentAnswerAll;
import com.xiaoshao.entity.StudentImproveAnswer;
import com.xiaoshao.mapper.StudentAnswerAllMapper;
import com.xiaoshao.service.IStudentAnswerAllService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-11
 */
@Service
public class StudentAnswerAllServiceImpl extends ServiceImpl<StudentAnswerAllMapper, StudentAnswerAll> implements IStudentAnswerAllService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLikeNumber(Long studentAnswerAllId, boolean isLike) {
        // 方案1：使用MP的UpdateWrapper（原子操作）
        UpdateWrapper<StudentAnswerAll> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", studentAnswerAllId)
                .setSql(isLike ? "like_number = like_number + 1" : "like_number = like_number - 1");

        if (!update(updateWrapper)) {
            throw new RuntimeException("更新点赞数失败");
        }
    }
    @Override
    @Transactional
    public Long createAnswerRecord(StudentAnswerAllDTO dto) {
//        TODO这里要加入错误处理
        // 1. 参数校验
        if (dto.getQuestionId() == null || !StringUtils.hasText(dto.getAnswerContent())) {
            throw new IllegalArgumentException("参数不合法");
        }

        // 2. 构建实体对象
        StudentAnswerAll entity = new StudentAnswerAll();
        entity.setQuestionId(dto.getQuestionId());
        entity.setAnswerContent(dto.getAnswerContent());

        entity.setStudentId(BaseContext.getCurrentId()); // 固定学生ID
        entity.setSubmitTime(LocalDateTime.now()); // 自动设置提交时间
        entity.setLikeNumber(0); // 默认点赞数为0

        // 3. 保存到数据库
        if (!save(entity)) {
            throw new RuntimeException("创建记录失败");
        }

        return entity.getId(); // 返回生成的主键ID
    }

}
