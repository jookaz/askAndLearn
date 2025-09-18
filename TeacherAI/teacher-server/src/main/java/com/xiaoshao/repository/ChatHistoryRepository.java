package com.xiaoshao.repository;

import java.util.List;

public interface ChatHistoryRepository {
    // 移除type参数
    void save(String chatId);
    List<String> getChatIds();
}