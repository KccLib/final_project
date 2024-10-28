package com.kcc.trioffice.global.handler;

import com.kcc.trioffice.domain.chat_room.dto.response.ChatRoomEnter;
import com.kcc.trioffice.domain.chat_status.service.ChatStatusService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final ChatStatusService chatStatusService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();


        if(accessor.getCommand() == StompCommand.CONNECT) {
        } else if(accessor.getCommand() == StompCommand.SUBSCRIBE) {
            log.info("Destination: {}", destination);

            if (destination != null && destination.contains("/sub/chat/room/")) {

                Long chatRoomId = Long.valueOf(destination.split("/")[4]);
                ChatRoomEnter chatRoomEnter = chatStatusService.enterChatRoom(chatRoomId, getEmployeeId(accessor));
                eventPublisher.publishEvent(chatRoomEnter);
            }

        } else if (accessor.getCommand() == StompCommand.UNSUBSCRIBE) {
            chatStatusService.disconnectChatRoom(getEmployeeId(accessor));

        } else if (accessor.getCommand() == StompCommand.DISCONNECT) {
            chatStatusService.disconnectChatRoom(getEmployeeId(accessor));
        }

        return message;
    }

    private Long getEmployeeId(StompHeaderAccessor accessor) {
        if (accessor.getUser() != null) {
            // SecurityContextHolder를 통해 Authentication 객체를 가져오기
            Authentication authentication = (Authentication) accessor.getUser();
            if (authentication != null && authentication.getPrincipal() != null) {
                // 여기서는 principal이 UserDetail 타입이라 가정
                return ((PrincipalDetail) authentication.getPrincipal()).getEmployeeId();
            }
        }
        throw new IllegalArgumentException("인증 정보가 없습니다.");
    }

}
