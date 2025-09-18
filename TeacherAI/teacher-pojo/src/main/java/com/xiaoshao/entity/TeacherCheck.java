package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教师预检表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teacher_check")
public class TeacherCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 6位工号
     */
    @TableId(value = "worker_number", type = IdType.AUTO)
    private String workerNumber;

    /**
     * 注册状态：0未注册/1已注册
     */
    private Integer isRegistered;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 所属学院
     */
    private String department;

    /**
     * 职称（教授/副教授/讲师等）
     */
    private String title;


}
