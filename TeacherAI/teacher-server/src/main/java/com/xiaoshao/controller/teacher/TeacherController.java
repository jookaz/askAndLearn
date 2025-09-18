package com.xiaoshao.controller.teacher;

import com.xiaoshao.constant.JwtClaimsConstant;
import com.xiaoshao.dto.StudentDTO;
import com.xiaoshao.dto.TeacherDTO;
import com.xiaoshao.dto.TeacherLoginDTO;
import com.xiaoshao.entity.Teacher;
import com.xiaoshao.properties.JwtProperties;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.ITeacherService;
import com.xiaoshao.service.impl.TeacherServiceImpl;
import com.xiaoshao.utils.JwtUtil;
import com.xiaoshao.vo.TeacherLoginVO;
import com.xiaoshao.vo.TeacherSelectVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Controller层
@RestController
@RequestMapping("/teacher/teacher")
@Tag(name = "教师管理", description = "教师管理")
@Slf4j
//TODO这里的代码结构肯定是由问题的，我这里也没有创建业务层接口，只有创建了业务层实现类
public class TeacherController {
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private JwtProperties jwtProperties;

    @Operation(summary = "获取所有教师列表（用于下拉框）")
    @GetMapping("/select")
    public Result<List<TeacherSelectVO>> getTeachersForSelect() {
        List<TeacherSelectVO> teacherList = teacherService.listTeacherSelect();
        return Result.success(teacherList);
    }
    /**
     *登录
     */
    @PostMapping("/login")
    public Result<TeacherLoginVO> login(@RequestBody TeacherLoginDTO teacherLoginDTO) {
        log.info("员工登录：{}",teacherLoginDTO);

        Teacher teacher = teacherService.login(teacherLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.TEACHER_ID,teacher.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getTeacherSecretKey(),
                jwtProperties.getTeacherTtl(),
                claims);

        TeacherLoginVO teacherLoginVO = TeacherLoginVO.builder()
                .id(teacher.getId())
                .teacherName(teacher.getTeacherName())
                .userName(teacher.getUserName())
                .title(teacher.getTitle())
                .department(teacher.getDepartment())
                .token(token)
                .build();
        return Result.success(teacherLoginVO);
    }
    /**
     * 教师注册接口
     * @param teacherDTO 包含工号、用户名、密码
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody TeacherDTO teacherDTO) {
        // 仅做参数传递，业务逻辑全交给Service
        Boolean result = teacherService.registerStudent(teacherDTO);
        if (!result) {
            return Result.error("注册失败，工号错误");
        }
        return Result.success("注册成功");
    }
}