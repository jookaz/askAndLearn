package com.xiaoshao.config;

import com.xiaoshao.factory.ModelFactory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
//    会话记忆功能，目前实现的是将会话记忆保存在内存中（系统实现，也可以自己实现）
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    // 提供获取模型工厂的Bean
    @Bean
    public ModelFactory modelFactory() {
        return new ModelFactory();
    }


}