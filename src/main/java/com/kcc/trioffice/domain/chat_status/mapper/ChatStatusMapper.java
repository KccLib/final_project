package com.kcc.trioffice.domain.chat_status.mapper;

import com.kcc.trioffice.domain.chat_status.dto.response.ChatStatusInfo;
import com.kcc.trioffice.domain.chat_status.dto.response.EmoticonStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatStatusMapper {

    int saveChatStatus(Long chatRoomId, Long chatId, Long employeeId, Long writer);

    int saveChatStatusRead(Long chatRoomId, Long chatId, Long employeeId, Long writer);

    Optional<ChatStatusInfo> getChatStatusByChatIdAndEmployeeId(Long chatId, Long employeeId);

    int updateEmoticon(Long chatId, Long employeeId, Long emoticonType, boolean isEmoticon);

    int updateChatStatusRead(Long chatRoomId, Long employeeId);

    Optional<EmoticonStatus> getEmoticonCount(Long chatId, Long employeeId);

    List<Long> getUnreadMessageId(Long chatRoomId, Long employeeId);
}
