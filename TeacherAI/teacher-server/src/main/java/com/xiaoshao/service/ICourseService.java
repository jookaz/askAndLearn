package com.xiaoshao.service;

import com.xiaoshao.dto.CourseDTO;
import com.xiaoshao.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.result.PageResult;
import com.xiaoshao.result.Result;
import com.xiaoshao.vo.CourseVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-21
 */
public interface ICourseService extends IService<Course> {
    List<Long> getCourseIds();
    void createCourse(CourseDTO courseDTO);
    void updateCourse(CourseDTO courseDTO);
    void deleteCourses(List<Long> courseIds);
    List<CourseVO> getCourseList();
    CourseVO getCourseDetail(Long courseId);
    PageResult<CourseVO> queryCoursesPage(CourseDTO courseDTO);
    PageResult<CourseVO> studentQueryCoursesPage(CourseDTO courseDTO);
    List<Map<String,Long>> getCourseNames();
    Long getCourseCount();
}
