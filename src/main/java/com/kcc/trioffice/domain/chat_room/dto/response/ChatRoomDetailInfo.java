package com.kcc.trioffice.domain.chat_room.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDetailInfo {

    private Long chatRoomId;
    private String chatRoomName;
    private String chatRoomProfileImageUrl;
    private long employeeStatus;
    private int participantCount;
    private List<ChatInfo> chatInfoList;
}
