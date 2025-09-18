//package com.xiaoshao.tools;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.xiaoshao.entity.StudentChapterWenxueValue;
//import com.xiaoshao.service.IStudentChapterWenxueValueService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.ai.tool.annotation.ToolParam;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class WenxueTools {
//    private final IStudentChapterWenxueValueService studentChapterWenxueValueService;
//    @Tool(description = "判断学生章节问学值记录是否存在")
//    public boolean existsStudentChapterWenxueValue(
//            @ToolParam(description = "学生ID") Long studentId,
//            @ToolParam(description = "课程ID") Long courseId,
//            @ToolParam(description = "章节ID") Long chapterId
//    ) {
//        // 构建查询条件
//        LambdaQueryWrapper<StudentChapterWenxueValue> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(StudentChapterWenxueValue::getStudentId, studentId)
//                .eq(StudentChapterWenxueValue::getCourseId, courseId)
//                .eq(StudentChapterWenxueValue::getChapterId, chapterId);
//
//        // 查询是否存在记录
//        return studentChapterWenxueValueService.count(queryWrapper) > 0;
//    }
//    @Tool(description = "创建学生章节问学值记录")
//    public Long createStudentChapterWenxueValue(
//            @ToolParam(description = "学生ID") Long studentId,
//            @ToolParam(description = "课程ID") Long courseId,
//            @ToolParam(description = "章节ID") Long chapterId,
//            @ToolParam(description = "问学值", required = false) Integer wenxueValue
//    ) {
//        // 设置默认问学值为0
//        if (wenxueValue == null) {
//            wenxueValue = 0;
//        }
//
//        // 创建实体对象
//        StudentChapterWenxueValue record = new StudentChapterWenxueValue();
//        record.setStudentId(studentId);
//        record.setCourseId(courseId);
//        record.setChapterId(chapterId);
//        record.setWenxueValue(wenxueValue);
//
//        // 调用服务层保存记录
//        boolean success = studentChapterWenxueValueService.save(record);
//        if (!success) {
//            throw new RuntimeException("创建问学值记录失败");
//        }
//
//        return record.getId(); // 返回新记录的主键ID
//    }
//    @Tool(description = "查询学生章节问学值记录")
//    public StudentChapterWenxueValue getStudentChapterWenxueValue(
//            @ToolParam(description = "学生ID") Long studentId,
//            @ToolParam(description = "课程ID") Long courseId,
//            @ToolParam(description = "章节ID") Long chapterId
//    ) {
//
//        return getRecord(studentId, courseId, chapterId);
//    }
//    @Tool(description = "更新学生章节问学值")
//    public boolean updateStudentChapterWenxueValue(
//            @ToolParam(description = "学生ID") Long studentId,
//            @ToolParam(description = "课程ID") Long courseId,
//            @ToolParam(description = "章节ID") Long chapterId,
//            @ToolParam(description = "问学值变化量", required = false) Integer delta
//    ) {
//        // 如果未传入delta，默认为0（不修改）
//        if (delta == null) {
//            delta = 0;
//        }
//
//        // 查询记录
//
//        StudentChapterWenxueValue record=  getRecord(studentId, courseId, chapterId);
//
//        // 更新问学值
//        int newWenxueValue = record.getWenxueValue() + delta;
//        record.setWenxueValue(newWenxueValue);
//
//        // 调用服务层更新
//        boolean success = studentChapterWenxueValueService.updateById(record);
//        if (!success) {
//            throw new RuntimeException("更新问学值记录失败");
//        }
//
//        return true;
//    }
//
//    private StudentChapterWenxueValue getRecord(@ToolParam(description = "学生ID（关联student.id）") Long studentId, @ToolParam(description = "课程ID（关联course.id）") Long courseId, @ToolParam(description = "章节ID（关联chapter.id）") Long chapterId) {
//        LambdaQueryWrapper<StudentChapterWenxueValue> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(StudentChapterWenxueValue::getStudentId, studentId)
//                .eq(StudentChapterWenxueValue::getCourseId, courseId)
//                .eq(StudentChapterWenxueValue::getChapterId, chapterId);
//        StudentChapterWenxueValue record = studentChapterWenxueValueService.getOne(queryWrapper);
//        if (record == null) {
//            throw new RuntimeException("未找到对应的问学值记录");
//        }
//        return record;
//    }
//}
