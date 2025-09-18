package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoshao.constant.MessageConstant;
import com.xiaoshao.dto.StudentDTO;
import com.xiaoshao.dto.TeacherDTO;
import com.xiaoshao.dto.TeacherLoginDTO;
import com.xiaoshao.entity.Student;
import com.xiaoshao.entity.StudentCheck;
import com.xiaoshao.entity.Teacher;
import com.xiaoshao.entity.TeacherCheck;
import com.xiaoshao.exception.AccountNotFoundException;
import com.xiaoshao.exception.PasswordErrorException;
import com.xiaoshao.mapper.TeacherMapper;
import com.xiaoshao.service.ITeacherService;
import com.xiaoshao.vo.TeacherSelectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherCheckServiceImpl teacherCheckService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean registerStudent(TeacherDTO teacherDTO) {
        // 1. 学号预检（包含存在性和注册状态校验）
        TeacherCheck teacherCheck = teacherCheckService.validateTeacherNumber(
                teacherDTO.getWorkerNumber());

//        // 2. 密码一致性校验（业务规则）
//        if (!studentDTO.getPassword().equals(studentDTO.getConfirmPassword())) {
//            throw new BusinessException("两次密码输入不一致");
//        }

        // 3. 数据组装
        Teacher teacher = buildTeacherEntity(teacherCheck, teacherDTO);

        // 4. 事务操作
        teacherCheckService.completeRegistration(teacherCheck);
        save(teacher);

        return true;
    }
    private Teacher buildTeacherEntity(TeacherCheck check, TeacherDTO dto) {
//        对密码进行md5加密
        return new Teacher()
                .setWorkerNumber(check.getWorkerNumber())
                .setTeacherName(check.getTeacherName())
                .setTitle(check.getTitle())
                .setDepartment(check.getDepartment())
                .setUserName(dto.getUserName())
                .setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes()));
    }
    @Override
    public List<TeacherSelectVO> listTeacherSelect() {
        // 查询所有教师，只返回需要的字段
        return teacherMapper.selectList(null)
            .stream()
            .map(teacher -> {
                TeacherSelectVO vo = new TeacherSelectVO();
                vo.setId(teacher.getId());
                vo.setName(teacher.getTeacherName());
                return vo;
            })
            .collect(Collectors.toList());
    }
    @Override
    public Teacher login(TeacherLoginDTO teacherLoginDTO) {
        String workerNumber = teacherLoginDTO.getWorkerNumber();
        String password = teacherLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
       Teacher teacher = teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("worker_number", workerNumber));

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (teacher == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对,要先对前端传来的密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(teacher.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return teacher;
    }
}