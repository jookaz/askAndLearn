package com.xiaoshao.controller.student;

import com.xiaoshao.context.BaseContext;
import com.xiaoshao.dto.StudentChapterWenxueValueDTO;
import com.xiaoshao.entity.StudentChapterWenxueValue;
import com.xiaoshao.result.Result;
import com.xiaoshao.service.IStudentChapterWenxueValueService;
import com.xiaoshao.vo.StudentChapterWenxueValueVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/student-chapter-wenxue-value")
@RequiredArgsConstructor
public class StudentChapterWenxueValueController {
    private final IStudentChapterWenxueValueService  service;
    @GetMapping("/get-by-courseId")
    public Result<StudentChapterWenxueValueVO> getByCourseId(StudentChapterWenxueValueDTO dto) {
        StudentChapterWenxueValueVO record = service.getRecord(BaseContext.getCurrentId(), dto.getCourseId());
        return Result.success(record);
    }
}
