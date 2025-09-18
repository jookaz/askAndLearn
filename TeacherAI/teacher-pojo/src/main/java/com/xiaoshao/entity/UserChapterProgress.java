package com.xiaoshao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaoshao
 * @since 2025-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_chapter_progress")
public class UserChapterProgress implements Serializable {

    private static final long serialVersionUID = 1L;
//    @TableId(type = IdType.AUTO)  // 添加单主键
//    private Long id;

    /**
     * 用户唯一标识（主键之一）
     */
    @MppMultiId
//    @TableField("user_id")
    private Long userId;

    @MppMultiId
//    @TableField("course_id")
    private Long courseId;

    @MppMultiId
//    @TableField("chapter_id")
    private Long chapterId;

    /**
     * 是否完成“提出问题”步骤
     */
    private Integer step1Complete;

    /**
     * 是否完成“模型回答”步骤
     */
    private Integer step2Complete;

    /**
     * 是否完成“用户回答”步骤
     */
    private Integer step3Complete;

    /**
     * 最后更新时间（用于判断用户是否需要重新开始流程）
     */

    private LocalDateTime lastUpdatedTime;


}
