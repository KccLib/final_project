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

    /**
     * 채팅방 상세 조회
     *
     * @param chatRoomId 조회할 채팅방 번호
     * @param limit 조회할 채팅 메세지 개수
     * @param offset 조회할 채팅 메세지 시작점
     * @param principalDetail 로그인한 사용자 정보
     * @return
     */
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDetailInfo> getChatRoomDetailInfo(@PathVariable Long chatRoomId,
                                                                    @RequestParam int limit,
                                                                    @RequestParam int offset,
                                                                    @AuthenticationPrincipal PrincipalDetail principalDetail) {

        ChatRoomDetailInfo chatRoomDetailInfo = chatRoomService
                .getChatRoomDetailInfo(chatRoomId, principalDetail.getEmployeeId(), limit, offset);

        return ResponseEntity.ok(chatRoomDetailInfo);
    }

    /**
     * 채팅 참여자 조회
     *
     * @param chatRoomId 조회할 채팅방 번호
     * @return 채팅 참여자 목록
     */
    @GetMapping("/{chatRoomId}/employees")
    public ResponseEntity<List<EmployeeInfo>> getChatRoomEmployees(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomEmployees(chatRoomId));
    }

    /**
     * 채팅방 목록 조회
     *
     * @param principalDetail
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ChatRoomInfo>> chatRoomList(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ResponseEntity.ok(chatRoomService.getChatRoomList(principalDetail.getEmployeeId(), null).getChatRoomInfos());
    }

    /**
     * 채팅방 퇴장
     *
     * @param chatRoomId 나갈 채팅방 번호
     * @param principalDetail 로그인한 사용자 정보
     */
    @DeleteMapping("/{chatRoomId}")
    public void deleteChatRoom(@PathVariable Long chatRoomId,
                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        chatRoomService.deleteChatRoom(chatRoomId, principalDetail.getEmployeeId());
    }

    /**
     * 채팅 삭제
     *
     * @param chatId 삭제할 채팅 번호
     * @param principalDetail 로그인한 사용자 정보
     */
    @DeleteMapping("/chats/{chatId}")
    public void deleteChat(@PathVariable Long chatId,
                           @AuthenticationPrincipal PrincipalDetail principalDetail) {
        chatRoomService.deleteChat(chatId, principalDetail.getEmployeeId());
    }

    /**
     * 채팅방 즐겨찾기
     *
     * @param chatRoomId 즐겨찾기할 채팅방 번호
     * @param principalDetail 로그인한 사용자 정보
     */
    @PostMapping("/{chatRoomId}/favorite")
    public void favoriteChatRoom(@PathVariable Long chatRoomId,
                                 @AuthenticationPrincipal PrincipalDetail principalDetail) {
        chatRoomService.favoriteChatRoom(chatRoomId, principalDetail.getEmployeeId());
    }

    /**
     * 즐겨찾기 한 채팅방 조회
     *
     * @param principalDetail 로그인한 사용자 정보
     * @return
     */
    @GetMapping("/favorite")
    public ResponseEntity<List<ChatRoomInfo>> getFavoriteChatRoom(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<ChatRoomInfo> favoriteChatRoom = chatRoomService.getFavoriteChatRoom(principalDetail.getEmployeeId());
        return ResponseEntity.ok(favoriteChatRoom);
    }

}
