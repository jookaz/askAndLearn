package com.xiaoshao.service;

import com.xiaoshao.dto.StudentDTO;
import com.xiaoshao.dto.StudentLoginDTO;
import com.xiaoshao.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.result.Result;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-11
 */
public interface IStudentService extends IService<Student> {
    /**
     * 员工登录
     * @param studentLoginDTO
     * @return
     */
    Student login(StudentLoginDTO studentLoginDTO);
    /**
     * 学生注册
     */
    Boolean registerStudent(StudentDTO studentDTO);
}
