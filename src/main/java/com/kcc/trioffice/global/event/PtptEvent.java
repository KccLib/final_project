package com.kcc.trioffice.global.event;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import com.kcc.trioffice.domain.chat_room.dto.response.*;
import com.kcc.trioffice.domain.chat_status.dto.response.EmoticonMessage;
import com.kcc.trioffice.domain.participation_employee.dto.response.PtptEmpInfos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PtptEvent {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleChatRoomEnterEvent(ChatRoomEnter chatRoomEnter) {

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatRoomEnter.getRoomId(), chatRoomEnter);
    }

    @EventListener
    public void handleChatRoomMessageEvent(PtptEmpInfos ptptEmpInfos) {

        ptptEmpInfos.getParticipantEmployeeInfos().forEach(participantEmployeeInfo -> {
            simpMessagingTemplate
                    .convertAndSend("/sub/chatrooms/employees/" + participantEmployeeInfo.getEmployeeId()
                            , participantEmployeeInfo);
        });
    }

    @EventListener
    public void handleChatRoomDeleteEvent(ChatDelete chatDelete) {

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatDelete.getRoomId(), chatDelete);
    }

    /**
     * 채팅방 생성 시 채팅방에 초대된 사람들에게 채팅방 생성 이벤트 전송
     * @param chatRoomCreate 채팅방 생성 정보
     */
    @EventListener
    public void handleSaveChatRoomEvent(ChatRoomCreate chatRoomCreate) {

        chatRoomCreate.getEmployees().forEach(empId -> {
            simpMessagingTemplate
                    .convertAndSend("/sub/chatrooms/employees/" + empId
                            , chatRoomCreate);
        });
    }

    @EventListener
    public void handleChatMessageEvent(ChatMessageInfo chatMessageInfo) {

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatMessageInfo.getRoomId(), chatMessageInfo);
    }

    @EventListener
    public void handleEmoticonEvent(EmoticonMessage emoticonMessage) {

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + emoticonMessage.getChatRoomId(), emoticonMessage);
    }

}
