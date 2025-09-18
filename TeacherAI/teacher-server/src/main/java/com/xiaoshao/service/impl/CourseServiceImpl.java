package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.context.BaseContext;
import com.xiaoshao.dto.ChapterDTO;
import com.xiaoshao.dto.CourseDTO;
import com.xiaoshao.entity.Chapter;
import com.xiaoshao.entity.Course;
import com.xiaoshao.entity.StudentCourseRelation;
import com.xiaoshao.entity.Teacher;
import com.xiaoshao.mapper.CourseMapper;
import com.xiaoshao.mapper.StudentCourseRelationMapper;
import com.xiaoshao.mapper.TeacherMapper;
import com.xiaoshao.result.PageResult;
import com.xiaoshao.service.IChapterService;
import com.xiaoshao.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoshao.service.IStudentCourseRelationService;
import com.xiaoshao.utils.BeanCopyUtil;
import com.xiaoshao.vo.ChapterVO;
import com.xiaoshao.vo.CourseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-21
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private IChapterService chapterService; // 注入章节服务
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private IStudentCourseRelationService studentCourseRelationService;
//    获取当前用户所创建的课程的所有id
    @Override
    public List<Long> getCourseIds(){
        Long creatUserId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getTeacherId, creatUserId);
        return list(queryWrapper).stream()
                .map(Course::getId)
                .toList();
    }

    @Override
    @Transactional
    public void createCourse(CourseDTO courseDTO) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        save(course); // 保存课程

        Long courseId = course.getId();
        List<ChapterDTO> chapters = courseDTO.getChapters(); // 获取章节列表
        if (chapters != null && !chapters.isEmpty()) {
            chapters.forEach(chapterDTO -> {
                chapterDTO.setCourseId(courseId); // 关联课程ID
                chapterService.createChapter(chapterDTO); // 保存章节
            });
        }
    }

    @Override
    @Transactional
    public void updateCourse(CourseDTO courseDTO) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        updateById(course); // 更新课程基本信息

        Long courseId = course.getId();
        List<ChapterDTO> chapters = courseDTO.getChapters();

        // 仅当章节数据存在时才处理章节
        if (chapters != null && !chapters.isEmpty()) {
            // 删除原有章节
            chapterService.deleteByCourseId(courseId);

            // 重新保存章节
            chapters.forEach(chapterDTO -> {
                chapterDTO.setCourseId(courseId);
                chapterService.createChapter(chapterDTO);
            });
        }
        // 否则，保留原有章节（不删除也不添加）
    }

    @Override
    public void deleteCourses(List<Long> courseIds) {
        if (CollectionUtils.isEmpty(courseIds)) {
            return;
        }

        // 1. 删除所有相关章节
        LambdaQueryWrapper<Chapter> chapterWrapper = new LambdaQueryWrapper<>();
        chapterWrapper.in(Chapter::getCourseId, courseIds);
        chapterService.remove(chapterWrapper);

        // 2. 删除课程本身
        removeByIds(courseIds);
    }

    @Override
    public List<CourseVO> getCourseList() {
        List<Course> list=list();
        return BeanCopyUtil.copyListProperties(list, CourseVO::new);
    }
    @Override
    public List<Map<String,Long>> getCourseNames() {
        Long currentUserId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getTeacherId, currentUserId);
        return list(queryWrapper).stream()
                .map(course -> {
                    Map<String, Long> map = new HashMap<>();
                    map.put(course.getCourseName(), course.getId()); // 课程名作为key，ID作为value
                    return map;
                })
                .toList();
    }
    @Override
    public CourseVO getCourseDetail(Long courseId) {
        Course course = getById(courseId);
        if (course == null) {
            return null;
        }
        CourseVO courseVO = new CourseVO();
        BeanUtils.copyProperties(course, courseVO);
        // 查询 teacher_name
        Teacher teacher = teacherMapper.selectOne(
                new QueryWrapper<Teacher>().eq("id", course.getTeacherId())
        );
        if (teacher != null) {
            courseVO.setTeacherName(teacher.getTeacherName());
        }

        // 获取章节列表并填充到VO
        List<ChapterVO> chapters = chapterService.getChapterListByCourseId(courseId);
        courseVO.setChapters(chapters); // 假设 CourseVO 有 setChapters 方法

        return courseVO;
    }
//TODO这里是支持结果排序的，所以如果想要使用这个功能需要前端进行控制，这里需要完成的是前端如何传递参数
    @Override
    public PageResult<CourseVO> queryCoursesPage(CourseDTO courseDTO) {
        Page<Course> page = courseDTO.toMpPageDefaultSortByUpdateTime();
        // 2. 构建查询条件：限制 teacherId = 当前用户ID
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getTeacherId, BaseContext.getCurrentId());
        // 3. 执行分页查询（带条件）
        page(page, queryWrapper);
//       封装返回
        return PageResult.of(page, CourseVO::new);
    }
//    学生分页查询课程
    @Override
    public PageResult<CourseVO> studentQueryCoursesPage(CourseDTO courseDTO) {
        LambdaQueryWrapper<StudentCourseRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentCourseRelation::getStudentId, BaseContext.getCurrentId());
        List<Long> courseIds = studentCourseRelationService.list(queryWrapper).
                stream().map(StudentCourseRelation::getCourseId).toList();
        if (CollectionUtils.isEmpty(courseIds)){
            return null;
        }
        Page<Course> page = courseDTO.toMpPageDefaultSortByUpdateTime();
        LambdaQueryWrapper<Course> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(Course::getId,courseIds);
        // 3. 执行分页查询（带条件）
        page(page,  queryWrapper1);
//       封装返回
        return PageResult.of(page, CourseVO::new);
    }
//    获取当前用户的课程总数
    @Override
    public Long getCourseCount() {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getTeacherId, BaseContext.getCurrentId());
        return count(queryWrapper);
    }
}
