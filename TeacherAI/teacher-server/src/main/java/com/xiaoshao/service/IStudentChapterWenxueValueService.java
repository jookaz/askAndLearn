package com.xiaoshao.service;

import com.xiaoshao.entity.StudentChapterWenxueValue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.vo.StudentChapterWenxueValueVO;

/**
 * <p>
 * 学生章节问学值表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-02
 */
public interface IStudentChapterWenxueValueService extends IService<StudentChapterWenxueValue> {
    /**
     * 判断学生章节问学值记录是否存在
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 是否存在记录
     */
    boolean existsRecord(Long studentId, Long courseId);

    /**
     * 创建学生章节问学值记录
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @param wenxueValue 问学值（可选，默认0）
     * @return 新记录的主键ID
     * @throws BusinessException 创建失败时抛出
     */
    Long createRecord(Long studentId, Long courseId,Integer wenxueValue)
            throws BusinessException;

    /**
     * 查询学生章节问学值记录
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 问学值记录实体
     * @throws BusinessException 记录不存在时抛出
     */
    StudentChapterWenxueValueVO getRecord(Long studentId, Long courseId)
            throws BusinessException;

    /**
     * 更新学生章节问学值
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 是否更新成功
     * @throws BusinessException 更新失败或记录不存在时抛出
     */
    boolean updateWenxueValue(Long studentId, Long courseId, Integer wenxueValue)
            throws BusinessException;

}
