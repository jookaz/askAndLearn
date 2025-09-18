package com.xiaoshao.service.impl;

import com.xiaoshao.entity.Student;
import com.xiaoshao.entity.StudentCheck;
import com.xiaoshao.exception.RegisterException;
import com.xiaoshao.mapper.StudentCheckMapper;
import com.xiaoshao.service.IStudentCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 学生预检表（增强版） 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-13
 */
@Service
public class StudentCheckServiceImpl extends ServiceImpl<StudentCheckMapper, StudentCheck> implements IStudentCheckService {
    public StudentCheck validateStudentNumber(String studentNumber) {
        StudentCheck check = lambdaQuery()
                .eq(StudentCheck::getStudentNumber, studentNumber)
                .oneOpt()
                .orElseThrow(() -> new RegisterException("学号不存在"));

        if (check.getIsRegistered() == 1) {
            throw new RegisterException("该学号已被注册");
        }
        return check;
    }


    public void completeRegistration(StudentCheck check) {
        check.setIsRegistered(1);
        updateById(check);  // 更新预检表状态

    }

}
