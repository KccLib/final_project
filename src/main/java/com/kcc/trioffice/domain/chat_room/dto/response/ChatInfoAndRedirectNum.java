package com.kcc.trioffice.domain.chat_room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatInfoAndRedirectNum {
    private List<ChatRoomInfo> chatRoomInfos;
    private Long redirectNum;
}
