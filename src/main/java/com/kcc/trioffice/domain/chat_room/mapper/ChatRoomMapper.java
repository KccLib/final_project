package com.kcc.trioffice.domain.chat_room.mapper;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatInfo;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatRoomDetailInfo;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatRoomInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatRoomMapper {

    int saveChatRoom(ChatRoomCreate chatRoomCreate, Long writer);

    List<ChatRoomInfo> getChatRoomListByEmployeeId(Long employeeId);

    Optional<ChatRoomDetailInfo> getChatRoomInfo(Long chatRoomId, Long employeeId);

    List<ChatInfo> getChatInfoByPage(Long chatRoomId, Long employeeId, int participantCount, int limit, int offset);

    int updateChatRoomLastMessage(Long chatRoomId, Long chatId);

    List<ChatRoomInfo> getFavoriteChatRooms(Long employeeId);

    Long getChatRoomIdByEmployeeIds(Long employeeId, Long targetEmployeeId);
}
