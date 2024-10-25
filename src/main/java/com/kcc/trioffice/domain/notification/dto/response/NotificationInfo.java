package com.kcc.trioffice.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationInfo {

    private Long notificationId;
    private Long employeeId;
    private Boolean notificationChecked;
    private Long relatedId;
    private Long type;
    private String title;
    private String contents;
    private Long writer;
    private String writeDt;
}
