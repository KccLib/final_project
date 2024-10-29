package com.kcc.trioffice.domain.chat_room.dto.request;

import com.kcc.trioffice.domain.schedule.dto.ComponentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChatRoomCreate extends ComponentDto {
    private Long chatRoomId;
    private String chatRoomName;
    private List<Long> employees;

}
