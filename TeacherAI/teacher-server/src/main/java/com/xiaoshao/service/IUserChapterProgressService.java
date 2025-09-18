package com.xiaoshao.service;

import com.github.jeffreyning.mybatisplus.service.IMppService;
import com.xiaoshao.entity.UserChapterProgress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.vo.UserChapterProgressVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-05
 */
public interface IUserChapterProgressService extends IMppService<UserChapterProgress> {
    /**
     * 查询用户进度记录，若不存在则创建新记录
     */
    UserChapterProgressVO getOrCreateProgress(Long userId, Long courseId, Long chapterId);

    /**
     * 更新用户进度状态
     */
    void updateProgressStatus(Long userId, Long courseId, Long chapterId, Integer step);

    /**
     * 获取用户的所有的进度记录
     * @return
     */
    List<UserChapterProgressVO> listProgressByUserIdAndCourseId(Long userId, Long courseId);

}
