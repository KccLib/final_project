package com.kcc.trioffice.domain.chat_room.dto.response;

public interface ChatRoomInfoBase {
    Long getChatRoomId();
    String getChatRoomName();
    void setChatRoomName(String chatRoomName);
    String getChatRoomProfileImageUrl();
    void setChatRoomProfileImageUrl(String chatRoomProfileImageUrl);
    Long getEmployeeStatus();
    void setEmployeeStatus(Long employeeStatus);
    int getParticipantCount();
}
