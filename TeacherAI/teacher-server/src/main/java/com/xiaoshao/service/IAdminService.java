package com.xiaoshao.service;

import com.xiaoshao.dto.AdminLoginDTO;
import com.xiaoshao.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.vo.AdminLoginVO;

/**
 * <p>
 * 系统管理员 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-24
 */
public interface IAdminService extends IService<Admin> {
    Admin login(AdminLoginDTO adminLoginDTO);

}
