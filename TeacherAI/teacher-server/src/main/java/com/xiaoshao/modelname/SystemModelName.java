package com.xiaoshao.modelname;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoshao.entity.AiModelConfig;
import com.xiaoshao.service.IAiModelConfigService;
import com.xiaoshao.service.IPromptTemplateService;
import com.xiaoshao.vo.PromptTemplateVO;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class SystemModelName {
    @Autowired
    private IAiModelConfigService aiModelConfigService;


    private String systemModelName;
    @PostConstruct // 在所有依赖注入完成后执行
    public void init() {
        //  获取aiModelConfigService表中system_flag属性为1的模型名称
        LambdaQueryWrapper<AiModelConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiModelConfig::getSystemFlag, 1);
        AiModelConfig aiModelConfig = aiModelConfigService.getOne(queryWrapper);
        if (aiModelConfig == null) {
            throw new RuntimeException("没有找到系统模型");
        }
        this.systemModelName = aiModelConfig.getModelName();
    }

}
