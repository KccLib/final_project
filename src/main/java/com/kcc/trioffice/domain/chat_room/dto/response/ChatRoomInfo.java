package com.kcc.trioffice.domain.chat_room.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class ChatRoomInfo implements Serializable {

    private Long chatRoomId;
    private String chatRoomName;
    private String chatRoomProfileImageUrl;
    private String lastMessage;
    private Long employeeStatus;
    private Timestamp lastMessageTime;
    private int unreadMessageCount;
    private int participantCount;
    @JsonProperty("isFavorited")
    private boolean isFavorited;

    public boolean getIsFavorited() {
        return isFavorited;
    }
}
