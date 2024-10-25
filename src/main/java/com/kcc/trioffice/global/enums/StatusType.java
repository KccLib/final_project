package com.kcc.trioffice.global.enums;

import com.kcc.trioffice.global.exception.type.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusType {
    ACTIVE(1L),
    ABSENT(2L),
    INACTIVE(3L),
    DO_NOT_DISTURB(4L);

    private final Long value;
    
    public static StatusType toEnum(Long value) {
        for (StatusType statusType : StatusType.values()) {
            if (statusType.getValue().equals(value)) {
                return statusType;
            }
        }
        throw new BadRequestException("Status 값이 잘못되었습니다.");
    }
    
    public static StatusType toEnum(String value) {
        for (StatusType statusType : StatusType.values()) {
            if (statusType.name().equals(value.toUpperCase())) {
                return statusType;
            }
        }
        throw new BadRequestException("Status 값이 잘못되었습니다.");
    }
}
