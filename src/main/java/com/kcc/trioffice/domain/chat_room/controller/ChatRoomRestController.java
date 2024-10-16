package com.kcc.trioffice.domain.chat_room.controller;

import com.kcc.trioffice.domain.chat_room.dto.response.*;
import com.kcc.trioffice.domain.chat_room.service.ChatRoomService;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatRoomRestController {

    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDetailInfo> getChatRoomDetailInfo(@PathVariable Long chatRoomId,
                                                                    @RequestParam int limit,
                                                                    @RequestParam int offset,
                                                                    @AuthenticationPrincipal PrincipalDetail principalDetail) {

        ChatRoomDetailInfo chatRoomDetailInfo = chatRoomService
                .getChatRoomDetailInfo(chatRoomId, principalDetail.getEmployeeId(), limit, offset);

        return ResponseEntity.ok(chatRoomDetailInfo);
    }

    @GetMapping("/{chatRoomId}/employees")
    public ResponseEntity<List<EmployeeInfo>> getChatRoomEmployees(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomEmployees(chatRoomId));
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomInfo>> chatRoomList(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ResponseEntity.ok(chatRoomService.getChatRoomList(principalDetail.getEmployeeId()));
    }

    @DeleteMapping("/{chatRoomId}")
    public void deleteChatRoom(@PathVariable Long chatRoomId,
                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        ChatMessageInfoAndPtptEmp chatMessageInfoAndPtptEmp = chatRoomService.deleteChatRoom(chatRoomId, principalDetail.getEmployeeId());

        chatMessageInfoAndPtptEmp.getParticipantEmployeeInfos().forEach(participantEmployeeInfo -> {
            simpMessagingTemplate
                    .convertAndSend("/sub/chatrooms/employees/" + participantEmployeeInfo.getEmployeeId()
                            , chatMessageInfoAndPtptEmp.getChatMessageInfo());
        });

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatRoomId, chatMessageInfoAndPtptEmp.getChatMessageInfo());

    }

}
