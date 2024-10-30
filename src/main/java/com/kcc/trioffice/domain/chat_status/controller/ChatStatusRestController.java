package com.kcc.trioffice.domain.chat_status.controller;

import com.kcc.trioffice.domain.chat_status.dto.request.UpdateEmoticon;
import com.kcc.trioffice.domain.chat_status.dto.response.EmoticonStatus;
import com.kcc.trioffice.domain.chat_status.service.ChatStatusService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@Slf4j
public class ChatStatusRestController {

    private final ChatStatusService chatStatusService;

    /**
     * 채팅방 이모티콘 업데이트
     *
     * @param chatRoomId 접속한 채팅방 번호
     * @param chatId 이모티콘 업데이트할 채팅
     * @param updateEmoticon 업데이트할 이모티콘 정보
     * @param principalDetail 로그인한 사용자 정보
     */
    @PostMapping("/chatrooms/{chatRoomId}/chats/{chatId}/emoticon")
    public void updateEmoticon(@PathVariable Long chatRoomId,
                               @PathVariable Long chatId,
                               @RequestBody UpdateEmoticon updateEmoticon,
                               @AuthenticationPrincipal PrincipalDetail principalDetail) {

        chatStatusService.updateEmoticon(chatId, chatRoomId, updateEmoticon.getEmoticonType(), principalDetail.getEmployeeId());
    }

    /**
     * 채팅방 이모티콘 정보 조회
     *
     * @param chatRoomId 접속한 채팅방 번호
     * @param chatId 이모티콘 정보 조회할 채팅
     * @param principalDetail 로그인한 사용자 정보
     * @return
     */
    @GetMapping("/chatrooms/{chatRoomId}/chats/{chatId}/emoticon")
    public ResponseEntity<EmoticonStatus> getEmoticonStatus(@PathVariable Long chatRoomId,
                                                            @PathVariable Long chatId,
                                                            @AuthenticationPrincipal PrincipalDetail principalDetail) {

        EmoticonStatus emoticonStatus = chatStatusService
                .getEmoticonCount(chatId, principalDetail.getEmployeeId());
        return ResponseEntity.ok(emoticonStatus);
    }

}
