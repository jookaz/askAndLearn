package com.xiaoshao.controller.student;

import com.xiaoshao.vo.MessageVO;
import com.xiaoshao.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    private final ChatHistoryRepository chatHistoryRepository;

    private final ChatMemory chatMemory;

    /**
     * 查询会话历史列表

     * @return chatId列表
     */
    @GetMapping
    public List<String> getChatIds() {
        return chatHistoryRepository.getChatIds();
    }

    /**
     * 根据业务类型、chatId查询会话历史
     * @param chatId 会话id
     * @return 指定会话的历史消息
     */
    @GetMapping("/{chatId}")
    public List<MessageVO> getChatHistory( @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId, Integer.MAX_VALUE);
        if(messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }
}