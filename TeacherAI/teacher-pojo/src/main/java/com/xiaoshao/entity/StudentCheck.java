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
 * 学生预检表（增强版）
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student_check")
public class StudentCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 6位学号
     */
    @TableId(value = "student_number", type = IdType.AUTO)
    private String studentNumber;

    /**
     * 注册状态：0未注册/1已注册
     */
    private Integer isRegistered;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 班级名称
     */
    private String classes;

    /**
     * 专业名称
     */
    private String major;


}
