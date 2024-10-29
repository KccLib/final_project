package com.kcc.trioffice.domain.chat_room.controller;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatMessage;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatMessageInfo;
import com.kcc.trioffice.domain.chat_room.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomSocketController {

    private final ChatRoomService chatRoomService;

    /**
     * 채팅 메세지 전송
     *
     * @param chatMessage 채팅 메세지 정보
     */
    @MessageMapping("/chat/send")
    public void sendChatMessage(ChatMessage chatMessage) {
        ChatMessageInfo chatMessageInfo = chatRoomService.saveChatMessage(chatMessage);
    }
}
