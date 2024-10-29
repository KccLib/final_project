package com.kcc.trioffice.domain.common.mapper;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatMessage;
import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatBotMapper {

    int saveChatRoom(ChatRoomCreate chatRoomCreate);

    int saveParticipationEmployee(Long chatRoomId, Long employeeId);

    int saveChatMessage(ChatMessage chatMessage);

    int checkChatBotRoom(Long type, Long employeeId);

    Long employeeRoomNumber(Long type, Long employeeId);
}
