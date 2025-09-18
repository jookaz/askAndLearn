package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教师表
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teacher")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 逻辑外键（对应user_base.id）
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long Id;
//TODO 目前teacher以及student这两张表都没有主键，我应该如何处理
    private String userName;

    private String password;

    private String teacherName;

    private String title;

    private String department;

   //工号
    private String workerNumber;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
