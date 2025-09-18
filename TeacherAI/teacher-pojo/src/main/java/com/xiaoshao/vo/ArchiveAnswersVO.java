package com.xiaoshao.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ArchiveAnswersVO implements Serializable {

    private Long id;

    /**
     * 答案内容
     */
    private String content;


}
