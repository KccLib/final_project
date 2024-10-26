package com.kcc.trioffice.domain.chat_room.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChatRoomCreate {

    private Long chatRoomId;
    private String chatRoomName;
    private List<Long> employees;

}
