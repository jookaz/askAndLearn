package com.xiaoshao.controller.teacher;


import com.xiaoshao.mapper.StudentCourseRelationMapper;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IStudentCourseRelationService;
import com.xiaoshao.vo.StudentCourseRelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-06-13
 */
@RestController
@RequestMapping("/student-course-relation")
public class StudentCourseRelationController {
    @Autowired
    private IStudentCourseRelationService  studentCourseRelationService;
    /**
     * 通过课程id获取学生信息
     */
    @GetMapping("/get-student-by-course-id/{courseId}")
    public Result<List<StudentCourseRelationVO>> getStudentByCourseId(@PathVariable Long courseId) {
        return Result.success(studentCourseRelationService.getStudentByCourseId(courseId));
    }

}
