package com.kcc.trioffice.domain.chat_room.mapper;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatInfo;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatRoomDetailInfo;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatRoomInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatRoomMapper {

    int saveChatRoom(@Param("chatRoomCreate") ChatRoomCreate chatRoomCreate, @Param("writer") Long writer);

    List<ChatRoomInfo> getChatRoomListByEmployeeId(Long employeeId);

    Optional<ChatRoomDetailInfo> getChatRoomInfo(@Param("chatRoomId") Long chatRoomId, @Param("employeeId") Long employeeId);

    List<ChatInfo> getChatInfoByPage(@Param("chatRoomId") Long chatRoomId, @Param("employeeId") Long employeeId, @Param("participantCount") int participantCount, @Param("limit") int limit, @Param("offset") int offset);

    int updateChatRoomLastMessage(@Param("chatRoomId") Long chatRoomId, @Param("chatId") Long chatId);

    List<ChatRoomInfo> getFavoriteChatRooms(Long employeeId);

    Long getChatRoomIdByEmployeeIds(Long employeeId, Long targetEmployeeId);

}
