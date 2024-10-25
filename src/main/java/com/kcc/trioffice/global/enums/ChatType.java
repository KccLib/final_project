package com.kcc.trioffice.global.enums;

import com.kcc.trioffice.global.exception.type.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatType {
    ENTER(1), CHAT(2), FILE(3), QUIT(4), IMAGE(5), CHAT_BOT(6);

    private final long value;

    public static long toValue(String chatType) {
        switch (chatType) {
            case "ENTER":
                return ENTER.value;
            case "CHAT":
                return CHAT.value;
            case "FILE":
                return FILE.value;
            case "QUIT":
                return QUIT.value;
            case "IMAGE":
                return IMAGE.value;
            case "CHAT_BOT":
                return CHAT_BOT.value;
            default:
                throw new BadRequestException("chatType이 잘못되었습니다.");
        }
    }

    public static String toName(long chatType) {
        switch ((int) chatType) {
            case 1:
                return ENTER.name();
            case 2:
                return CHAT.name();
            case 3:
                return FILE.name();
            case 4:
                return QUIT.name();
            case 5:
                return IMAGE.name();
            case 6:
                return CHAT_BOT.name();
            default:
                throw new BadRequestException("chatType이 잘못되었습니다.");
        }
    }

    public static Long convertFileType(Long fileType) {
        switch (fileType.intValue()) {
            case 1:
                return IMAGE.value;
            case 2:
                return FILE.value;
            default:
                throw new BadRequestException("fileType이 잘못되었습니다.");
        }
    }
}
