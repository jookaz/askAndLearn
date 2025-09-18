package com.xiaoshao.service.impl;

import com.xiaoshao.entity.StudentCheck;
import com.xiaoshao.entity.TeacherCheck;
import com.xiaoshao.exception.RegisterException;
import com.xiaoshao.mapper.TeacherCheckMapper;
import com.xiaoshao.service.ITeacherCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 教师预检表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-16
 */
@Service
public class TeacherCheckServiceImpl extends ServiceImpl<TeacherCheckMapper, TeacherCheck> implements ITeacherCheckService {
    public TeacherCheck validateTeacherNumber(String workerNumber) {
        TeacherCheck check = lambdaQuery()
                .eq(TeacherCheck::getWorkerNumber, workerNumber)
                .oneOpt()
                .orElseThrow(() -> new RegisterException("工号不存在"));

        if (check.getIsRegistered() == 1) {
            throw new RegisterException("该工号已被注册");
        }
        return check;
    }


    public void completeRegistration(TeacherCheck check) {
        check.setIsRegistered(1);
        updateById(check);  // 更新预检表状态

    }

}
