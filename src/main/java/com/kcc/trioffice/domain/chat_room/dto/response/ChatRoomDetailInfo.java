package com.kcc.trioffice.domain.chat_room.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDetailInfo implements ChatRoomInfoBase {

    private Long chatRoomId;
    private String chatRoomName;
    private String chatRoomProfileImageUrl;
    private Long employeeStatus;
    private int participantCount;
    private List<ChatInfo> chatInfoList;

}
