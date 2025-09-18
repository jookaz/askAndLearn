package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.xiaoshao.dto.ChapterDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long teacherId;

    private String courseName;

    private String description;

    private String image;

    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT,value = "create_user")
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE,  value = "update_time")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_user")
    private Long updateUser;


}
