package com.kcc.trioffice.domain.chat_status.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmoticonMessage {
    private String chatType;
    private Long chatRoomId;
    private Long chatId;
    private Long chatterId;
    private Long senderId;
    private String emoticonType;
    private Boolean isEmoticon;

}
