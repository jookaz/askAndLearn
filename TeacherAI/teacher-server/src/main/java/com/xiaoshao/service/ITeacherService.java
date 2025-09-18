package com.xiaoshao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.dto.StudentDTO;
import com.xiaoshao.dto.StudentLoginDTO;
import com.xiaoshao.dto.TeacherDTO;
import com.xiaoshao.dto.TeacherLoginDTO;
import com.xiaoshao.entity.Student;
import com.xiaoshao.entity.Teacher;
import com.xiaoshao.vo.TeacherSelectVO;

import java.util.List;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-11
 */
public interface ITeacherService extends IService<Teacher> {
    /**
     * 员工登录
     * @param teacherLoginDTO
     * @return
     */
    Teacher login(TeacherLoginDTO teacherLoginDTO);
    /**
     * 教师注册
     */
    Boolean registerStudent(TeacherDTO teacherDTO);

    /**
     * 获取所有教师表
     * @return
     */
    List<TeacherSelectVO> listTeacherSelect();
}
