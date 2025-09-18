package com.xiaoshao.repository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {
    private final Set<String> chatIds = new HashSet<>();

    @Override
    public void save(String chatId) {
        chatIds.add(chatId);
    }

    @Override
    public List<String> getChatIds() {
        return new ArrayList<>(chatIds);
    }
}