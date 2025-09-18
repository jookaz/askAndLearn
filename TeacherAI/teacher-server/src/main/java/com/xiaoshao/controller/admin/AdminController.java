package com.xiaoshao.controller.admin;


import com.xiaoshao.constant.JwtClaimsConstant;
import com.xiaoshao.dto.AdminLoginDTO;
import com.xiaoshao.dto.TeacherLoginDTO;
import com.xiaoshao.entity.Admin;
import com.xiaoshao.entity.Teacher;
import com.xiaoshao.properties.JwtProperties;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IAdminService;
import com.xiaoshao.service.ITeacherService;
import com.xiaoshao.utils.JwtUtil;
import com.xiaoshao.vo.AdminLoginVO;
import com.xiaoshao.vo.TeacherLoginVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 系统管理员 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-24
 */
@RestController
@Tag(name = "管理员管理", description = "管理员管理")
@Slf4j
@RequestMapping("/admin/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private JwtProperties jwtProperties;
    /**
     *登录
     */
    @PostMapping("/login")
    public Result<AdminLoginVO> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("管理员登录：{}",adminLoginDTO);

        Admin admin = adminService.login(adminLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ADMIN_ID,admin.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        AdminLoginVO adminLoginVO = AdminLoginVO.builder()
                .id(admin.getId())
                .adminName(admin.getAdminName())
                .token(token)
                .build();
        return Result.success(adminLoginVO);
    }

}
