package com.kcc.trioffice.domain.chat_room.mapper;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatMessage;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatDetailInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ChatMapper {

    int saveChatMessage(ChatMessage chatMessage);

    Optional<ChatDetailInfo> getChatDetailInfo(Long chatId);

    int deleteChatMessage(Long chatId, Long employeeId);

}
