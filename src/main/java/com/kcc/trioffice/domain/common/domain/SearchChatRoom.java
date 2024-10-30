package com.kcc.trioffice.domain.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SearchChatRoom {

    private String name;
    private String imageURL;

    public SearchChatRoom(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }
}

