package com.xiaoshao.factory;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoshao.entity.AiModelConfig;
import com.xiaoshao.service.IAiModelConfigService;
import com.xiaoshao.service.ICourseService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ModelFactory {
    @Autowired
    private IAiModelConfigService  aiModelConfigService;
    @Autowired
    private ICourseService  courseService;

    // 缓存已创建的模型配置
    private final Map<String, ChatModel> modelCache = new HashMap<>();
//获取模型配置
    public ChatModel getChatModel(String modelName) {
        AiModelConfig  aiModelConfig = aiModelConfigService.getOne(
                new LambdaQueryWrapper<AiModelConfig>().eq(AiModelConfig::getModelName, modelName));

        if ( aiModelConfig == null) {
            throw new IllegalArgumentException("模型不存在: " + modelName);
        }

        // 1. 创建 OpenAiApi 实例，设置 baseUrl 和 apiKey
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl( aiModelConfig.getBaseUrl()) // 设置基础 URL（关键步骤）
                .apiKey( aiModelConfig.getApiKey())   // 设置 API 密钥
                .build();

        // 2. 使用 OpenAiApi 配置 ChatModel
        return modelCache.computeIfAbsent( aiModelConfig.getModelName() , key -> OpenAiChatModel.builder()
                .openAiApi(openAiApi)     // 传递配置好的 OpenAiApi 实例
                .defaultOptions(OpenAiChatOptions.builder()
//                        TODO这里有一个小问题就是我发现在同一次服务器当中只要询问的问题一样回答也就一样，但是重新启动后回答就不一样了
                        .temperature(1.0)
                        .topP(0.7)
                        .model( aiModelConfig.getModelName()) // 设置模型名称（如 qwen-max-latest）
                        .build())
                .build());
    }


//获取系统有的所有的模型的信息（名字）
    public List<String> getAvailableModels(Long courseId) {
        Long teacherId=courseService.getById(courseId).getTeacherId();
        // 2. 构建动态查询条件：教师创建的模型 OR 系统模型
        LambdaQueryWrapper<AiModelConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(AiModelConfig::getCreateUser, teacherId) // 教师自己的模型
                .or() // 或条件
                .eq(AiModelConfig::getCreateUser, 0);       // 系统模型（create_user=0）

        List<AiModelConfig> aiModelConfigs = aiModelConfigService.list(queryWrapper);
        return aiModelConfigs.stream()
                .map(AiModelConfig::getModelName)
                .collect(Collectors.toList());
    }
}