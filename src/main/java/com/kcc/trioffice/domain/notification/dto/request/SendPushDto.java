package com.kcc.trioffice.domain.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendPushDto {

    private String title;
    private String content;
    private String image;

    public static SendPushDto of(String chatRoomName, String content, String image) {
        return new SendPushDto(chatRoomName, content, image);
    }

}
