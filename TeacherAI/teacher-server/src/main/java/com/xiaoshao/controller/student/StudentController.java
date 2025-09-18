package com.xiaoshao.controller.student;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoshao.constant.JwtClaimsConstant;
import com.xiaoshao.context.BaseContext;
import com.xiaoshao.dto.StudentDTO;
import com.xiaoshao.dto.StudentLoginDTO;
import com.xiaoshao.entity.Student;
import com.xiaoshao.entity.StudentCheck;
import com.xiaoshao.properties.JwtProperties;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IStudentService;
import com.xiaoshao.utils.JwtUtil;
import com.xiaoshao.vo.StudentLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-11
 */
@RestController
@Slf4j
@RequestMapping("/student/student")
public class StudentController {
    @Autowired
    private IStudentService studentService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     *登录
     */
    @PostMapping("/login")
    public Result<StudentLoginVO> login(@RequestBody StudentLoginDTO studentLoginDTO) {
        log.info("学生登录：{}",studentLoginDTO);

        Student student = studentService.login(studentLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.STUDENT_ID,student.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getStudentSecretKey(),
                jwtProperties.getStudentTtl(),
                claims);

        StudentLoginVO studentLoginVO = StudentLoginVO.builder()
                .id(student.getId())
                .studentName(student.getStudentName())
                .classes(student.getClasses())
                .major(student.getMajor())
                .token(token)
                .build();

        return Result.success(studentLoginVO);
    }
    /**
     * 学生注册接口
     * @param studentDTO 包含学号、用户名、密码
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody StudentDTO studentDTO) {
        // 仅做参数传递，业务逻辑全交给Service
        Boolean result = studentService.registerStudent(studentDTO);
        if (!result) {
            return Result.error("注册失败，学号错误");
        }
       return Result.success("注册成功");
    }
//    获取登录学生的信息
    @GetMapping("/get-by-id")
    public Result<Student> getById() {
        Student student = studentService.getById(BaseContext.getCurrentId());
//        System.out.println(student);
//        System.out.println(BaseContext.getCurrentId());
        return Result.success(student);
    }

}
