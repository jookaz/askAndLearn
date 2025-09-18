package com.xiaoshao.vo;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserChapterProgressVO implements Serializable {
    private Long userId;


    private Long courseId;

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
