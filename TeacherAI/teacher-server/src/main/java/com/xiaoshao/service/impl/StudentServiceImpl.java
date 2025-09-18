package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoshao.constant.MessageConstant;
import com.xiaoshao.dto.StudentDTO;
import com.xiaoshao.dto.StudentLoginDTO;
import com.xiaoshao.entity.Student;
import com.xiaoshao.entity.StudentCheck;
import com.xiaoshao.exception.AccountNotFoundException;
import com.xiaoshao.exception.PasswordErrorException;
import com.xiaoshao.mapper.StudentMapper;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;



/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-11
 */
@Service
@AllArgsConstructor
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    private final StudentCheckServiceImpl studentCheckService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean registerStudent(StudentDTO studentDTO) {
        // 1. 学号预检（包含存在性和注册状态校验）
        StudentCheck studentCheck = studentCheckService.validateStudentNumber(
                studentDTO.getStudentNumber());

//        // 2. 密码一致性校验（业务规则）
//        if (!studentDTO.getPassword().equals(studentDTO.getConfirmPassword())) {
//            throw new BusinessException("两次密码输入不一致");
//        }

        // 3. 数据组装
        Student student = buildStudentEntity(studentCheck, studentDTO);

        // 4. 事务操作
        studentCheckService.completeRegistration(studentCheck);
        save(student);

        return true;
    }

    private Student buildStudentEntity(StudentCheck check, StudentDTO dto) {
//        对密码进行md5加密
        return new Student()
                .setStudentNumber(check.getStudentNumber())
                .setStudentName(check.getStudentName())
                .setClasses(check.getClasses())
                .setMajor(check.getMajor())
                .setUserName(dto.getUserName())
                .setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes()));
    }
    /**
     * 员工登录
     */
    public Student login(StudentLoginDTO studentLoginDTO) {
        String studentNumber = studentLoginDTO.getStudentNumber();
        String password = studentLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Student student = getOne(new QueryWrapper<Student>().eq("student_number", studentNumber));

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (student == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对,要先对前端传来的密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(student.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return student;
    }


}
