package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoshao.constant.MessageConstant;
import com.xiaoshao.dto.AdminLoginDTO;
import com.xiaoshao.dto.TeacherLoginDTO;
import com.xiaoshao.entity.Admin;
import com.xiaoshao.entity.Teacher;
import com.xiaoshao.exception.AccountNotFoundException;
import com.xiaoshao.exception.PasswordErrorException;
import com.xiaoshao.mapper.AdminMapper;
import com.xiaoshao.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 系统管理员 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-24
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public Admin login(AdminLoginDTO adminLoginDTO) {
        String workerNumber = adminLoginDTO.getWorkerNumber();
        String password = adminLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("worker_number", workerNumber));

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (admin == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对,要先对前端传来的密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(admin.getPassword())) {
            System.out.println(admin.getPassword());
            System.out.println(password);
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对
        return admin;
    }
}