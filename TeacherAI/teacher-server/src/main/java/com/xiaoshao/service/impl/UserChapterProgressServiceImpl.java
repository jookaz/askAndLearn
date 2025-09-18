package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.xiaoshao.entity.UserChapterProgress;
import com.xiaoshao.mapper.UserChapterProgressMapper;
import com.xiaoshao.service.IUserChapterProgressService;
import com.xiaoshao.utils.BeanCopyUtil;
import com.xiaoshao.vo.UserChapterProgressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-05
 */
@Service
public class UserChapterProgressServiceImpl extends MppServiceImpl<UserChapterProgressMapper, UserChapterProgress>
        implements IUserChapterProgressService {
    /**
     * 查询用户进度记录，若不存在则创建新记录
     */
    @Override
    public UserChapterProgressVO getOrCreateProgress(Long userId, Long courseId, Long chapterId) {
        // 构造联合主键条件对象
        UserChapterProgress condition = new UserChapterProgress()
                .setUserId(userId)
                .setCourseId(courseId)
                .setChapterId(chapterId);

        // 使用 MPP 的条件查询方法
        UserChapterProgress progress = this.getOne(new QueryWrapper<>(condition));

        if (progress == null) {
            progress = new UserChapterProgress()
                    .setUserId(userId)
                    .setCourseId(courseId)
                    .setChapterId(chapterId)
                    .setStep1Complete(0)
                    .setStep2Complete(0)
                    .setStep3Complete(0)
                    .setLastUpdatedTime(LocalDateTime.now());
            this.save(progress);
        }

        UserChapterProgressVO vo = new UserChapterProgressVO();
        BeanUtils.copyProperties(progress, vo);
        return vo;
    }

    @Override
    @Transactional
    public void updateProgressStatus(Long userId, Long courseId, Long chapterId, Integer step) {
        // 1. 获取或创建进度记录
        UserChapterProgress progress = this.getOne(new QueryWrapper<>(new UserChapterProgress()
                .setUserId(userId)
                .setCourseId(courseId)
                .setChapterId(chapterId)));

        if (progress == null) {
            progress = new UserChapterProgress()
                    .setUserId(userId)
                    .setCourseId(courseId)
                    .setChapterId(chapterId);
        }

        // 2. 更新状态
        switch (step) {
            case 1: progress.setStep1Complete(1); break;
            case 2: progress.setStep2Complete(1); break;
            case 3: progress.setStep3Complete(1); break;
            default: throw new IllegalArgumentException("无效的步骤参数");
        }

        // 3. 保存更新（使用条件更新）
        progress.setLastUpdatedTime(LocalDateTime.now());
            this.saveOrUpdateByMultiId(progress);
    }
    public List<UserChapterProgressVO> getAllProgress() {
        // 查询所有记录（无分页条件）
        List<UserChapterProgress> progressList = this.list();

        // 转换为VO列表
        return progressList.stream()
                .map(progress -> {
                    UserChapterProgressVO vo = new UserChapterProgressVO();
                    BeanUtils.copyProperties(progress, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<UserChapterProgressVO> listProgressByUserIdAndCourseId(Long userId,  Long courseId) {
        // 1. 查询该用户的所有记录
        List<UserChapterProgress> progressList = this.list(
                new QueryWrapper<UserChapterProgress>()
                        .eq("user_id", userId)
                        .eq("course_id", courseId)
        );
        return BeanCopyUtil.copyListProperties(progressList, UserChapterProgressVO::new);

    }

}
