package com.kcc.trioffice.domain.chat_room.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private Long roomId;
    private Long senderId;
    private String message;
    private Long chatType;
    private Long chatId;

}
