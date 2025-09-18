package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoshao.entity.StudentChapterWenxueValue;
import com.xiaoshao.exception.BusinessException;
import com.xiaoshao.mapper.StudentChapterWenxueValueMapper;
import com.xiaoshao.service.IStudentChapterWenxueValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xiaoshao.vo.StudentChapterWenxueValueVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 学生章节问学值表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-02
 */
@Service
public class StudentChapterWenxueValueServiceImpl extends ServiceImpl<StudentChapterWenxueValueMapper, StudentChapterWenxueValue> implements IStudentChapterWenxueValueService {
    @Override
    public boolean existsRecord(Long studentId, Long courseId) {
        LambdaQueryWrapper<StudentChapterWenxueValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentChapterWenxueValue::getStudentId, studentId)
                .eq(StudentChapterWenxueValue::getCourseId, courseId);
        return count(queryWrapper) > 0;
    }

    @Override
    public Long createRecord(Long studentId, Long courseId, Integer wenxueValue) {
        // 设置默认值
        int finalValue = Optional.ofNullable(wenxueValue).orElse(0);

        // 构造实体对象
        StudentChapterWenxueValue record = new StudentChapterWenxueValue()
                .setStudentId(studentId)
                .setCourseId(courseId)
                .setWenxueValue(finalValue);

        // 保存记录
        if (!save(record)) {
            throw new BusinessException("问学值记录创建失败");
        }
        return record.getId();
    }

    @Override
    public StudentChapterWenxueValueVO getRecord(Long studentId, Long courseId) {
        LambdaQueryWrapper<StudentChapterWenxueValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentChapterWenxueValue::getStudentId, studentId)
                .eq(StudentChapterWenxueValue::getCourseId, courseId);
        StudentChapterWenxueValue record = getOne(queryWrapper);
        if (record == null) {
            throw new BusinessException("未找到对应的问学值记录");
        }
        StudentChapterWenxueValueVO recordVO = new StudentChapterWenxueValueVO();
        BeanUtils.copyProperties(record,recordVO);
        return recordVO;
    }

    @Override
    public boolean updateWenxueValue(Long studentId, Long courseId,Integer wenxueValue) {
        // 获取记录
        StudentChapterWenxueValueVO record = getRecord(studentId, courseId);
        StudentChapterWenxueValue recordEntity = new StudentChapterWenxueValue();
        BeanUtils.copyProperties(record,recordEntity);
        // 计算新值
        int newValue = recordEntity.getWenxueValue() + Optional.ofNullable(wenxueValue).orElse(0);
        recordEntity.setWenxueValue(newValue);

        // 更新记录
        if (!updateById(recordEntity)) {
            throw new BusinessException("问学值更新失败");
        }
        return true;
    }
}
