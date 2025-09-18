package com.xiaoshao.service;

import com.xiaoshao.entity.StudentCourseRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.vo.StudentCourseRelationVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-18
 */
public interface IStudentCourseRelationService extends IService<StudentCourseRelation> {
    List<StudentCourseRelationVO> getStudentByCourseId(Long courseId);

}
