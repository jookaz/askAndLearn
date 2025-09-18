package com.xiaoshao.service;

import com.xiaoshao.dto.StudentAnswerAllDTO;
import com.xiaoshao.entity.StudentAnswerAll;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-11
 */
public interface IStudentAnswerAllService extends IService<StudentAnswerAll> {
    /**
     * 更新问题点赞数
     * @param isLike 是否点赞
     */
    void updateLikeNumber(Long studentAnswerAllId, boolean isLike);
    /**
     * 创建学生答案记录
     * @param dto 传输对象
     * @return 生成的主键ID
     */
    Long createAnswerRecord(StudentAnswerAllDTO dto);

}
