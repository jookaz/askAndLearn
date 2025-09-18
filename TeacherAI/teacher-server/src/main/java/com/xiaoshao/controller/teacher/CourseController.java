package com.xiaoshao.controller.teacher;


import com.xiaoshao.dto.CourseDTO;
import com.xiaoshao.service.ICourseService;
import com.xiaoshao.vo.CourseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xiaoshao.result.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程表 前端控制器
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-21
 */
@Tag(name = "课程管理", description = "课程管理")
@RestController("teacherCourseController")
@Slf4j
@RequestMapping("/teacher/courses")
//TODO 关于前端学生部分的页面功能调用了教师端的接口，需要优化，是不是要在学生端也创建相关的接口，这样更好
public class CourseController {
    @Autowired
    private ICourseService courseService;

    /**
     * 创建课程
     * @param courseDTO
     * @return
     */
    @Operation(summary = "创建课程")
    @PostMapping
    public Result<?> createCourse(@RequestBody CourseDTO courseDTO) {
        courseService.createCourse(courseDTO);
        return Result.success();
    }

    /**
     * 更新课程
     * @param courseDTO
     * @return
     */
    @Operation(summary = "更新课程")
    @PutMapping
    public Result<?> updateCourse(@RequestBody CourseDTO courseDTO) {
        courseService.updateCourse(courseDTO);
        return Result.success();
    }

    /**
     * 批量删除课程
     * @param courseIds
     * @return
     */
    @DeleteMapping
    @Operation(summary = "批量删除课程")
    public Result<?> deleteCourses(@RequestParam("ids") List<Long> courseIds) {
        courseService.deleteCourses(courseIds);
        return Result.success();
    }

    /**
     * 得到课程列表，目前这个接口没有得到使用
     * @return
     */
    @Operation(summary = "得到课程列表")
    @GetMapping
    public Result<?> getCourseList() {
        List<CourseVO> list = courseService.getCourseList();
        return Result.success(list);
    }
    /**
     * 得到全部的课程名称列表，这个在问题管理中使用用来进行条件分页查询
     */
    @Operation(summary = "得到全部的课程名称列表")
    @GetMapping("/names")
    public Result<?> getCourseNames() {
        List<Map<String,Long>> courseNames = courseService.getCourseNames();
        return Result.success(courseNames);
    }

    /**
     * 根据课程id得到课程详情
     * @param courseId
     * @return
     */
    @Operation(summary = "根据课程id得到课程详情")
    @GetMapping("/{courseId}")
    public Result<?> getCourseDetail(@PathVariable Long courseId) {
        CourseVO courseVO =courseService.getCourseDetail(courseId);
        return Result.success(courseVO);
    }
    /**
     * 分页查询
     * @param courseDTO
     * @return
     */
//    TODO这里的文档有问题，前端传入的是简单请求参数，但是文档中写的是一个对象
//    TODO前端是否要完成搜索框的功能
//    http://localhost:8080/teacher/courses/page?pageNo=1&pageSize=2
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<?> page(CourseDTO courseDTO) {
        return Result.success(courseService.queryCoursesPage(courseDTO));
    }
    /**
     * 获取课程总数
     * @return
     */
    @Operation(summary = "获取课程总数")
    @GetMapping("/count")
    public Result<?> getCourseCount() {
        return Result.success(courseService.getCourseCount());
    }

}
