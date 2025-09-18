package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoshao.entity.StudentChapterWenxueValue;
import com.xiaoshao.entity.StudentCourseRelation;
import com.xiaoshao.mapper.StudentCourseRelationMapper;
import com.xiaoshao.service.IStudentChapterWenxueValueService;
import com.xiaoshao.service.IStudentCourseRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoshao.service.IStudentService;
import com.xiaoshao.vo.StudentCourseRelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-18
 */
@Service
public class StudentCourseRelationServiceImpl extends ServiceImpl<StudentCourseRelationMapper, StudentCourseRelation> implements IStudentCourseRelationService {
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IStudentChapterWenxueValueService  studentChapterWenxueValueService;
    @Override
    public List<StudentCourseRelationVO> getStudentByCourseId(Long courseId) {
        // First query to get student IDs enrolled in the course
        LambdaQueryWrapper<StudentCourseRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentCourseRelation::getCourseId, courseId);
        queryWrapper.select(StudentCourseRelation::getStudentId);
        List<Long> studentIds = list(queryWrapper).stream()
                .map(StudentCourseRelation::getStudentId)
                .collect(Collectors.toList());

        // If no students found, return empty list
        if (studentIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Get student details and map to VO objects
        List<StudentCourseRelationVO> studentCourseRelationVOS = studentService.listByIds(studentIds).stream()
                .map(student -> {
                    StudentCourseRelationVO vo = new StudentCourseRelationVO();
                    vo.setStudentName(student.getStudentName());
                    vo.setClasses(student.getClasses());
                    vo.setStudentId(student.getId());
                    return vo;
                })
                .toList();
//        给每一个StudentCourseRelationVO对象添加wenxueValue属性
        studentCourseRelationVOS.forEach(vo -> {
            LambdaQueryWrapper<StudentChapterWenxueValue> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(StudentChapterWenxueValue::getStudentId, vo.getStudentId());
            queryWrapper1.eq(StudentChapterWenxueValue::getCourseId, courseId);
            if (studentChapterWenxueValueService.getOne(queryWrapper1) == null) {
                vo.setWenxueValue(0);
            }else {
                StudentChapterWenxueValue studentChapterWenxueValue = studentChapterWenxueValueService.getOne(queryWrapper1);
                vo.setWenxueValue(studentChapterWenxueValue.getWenxueValue());
            }
        });
        return studentCourseRelationVOS;
    }
}
