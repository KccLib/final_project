package com.kcc.trioffice.domain.chat_room.dto.request;

import com.kcc.trioffice.domain.schedule.dto.ComponentDto;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomCreate extends ComponentDto {

    private Long chatRoomId;
    private String chatRoomName;
    private List<Long> employees;

}
