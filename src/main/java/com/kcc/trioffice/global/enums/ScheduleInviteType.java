package com.kcc.trioffice.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ScheduleInviteType {
    WAITING(0L), // 초대
    NOT_PARTICIPATE(1L), // 불참
    PARTICIPATE(2L); // 참석


    private long value;

}
