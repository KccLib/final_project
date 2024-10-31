package com.kcc.trioffice.domain.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class SearchChatRoom {

    private String name;
    private String imageURL;
    private Long myChatRoomId;

    public SearchChatRoom(String name, String imageURL, Long myChatRoomId) {
        this.name = name;
        this.imageURL = imageURL;
        this.myChatRoomId = myChatRoomId;
    }
}

