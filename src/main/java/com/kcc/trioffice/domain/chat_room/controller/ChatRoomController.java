package com.kcc.trioffice.domain.chat_room.controller;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatInfoAndRedirectNum;
import com.kcc.trioffice.domain.chat_room.service.ChatRoomService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 목록 조회
     *
     * targetId가 있을 경우 해당 채팅방으로 바로 이동
     *
     * @param model
     * @param principalDetail
     * @param targetId 대화하고자 하는 사원 번호
     * @return
     */
    @GetMapping("/chatrooms")
    public String chatRoomList(Model model,
                               @AuthenticationPrincipal PrincipalDetail principalDetail,
                               @RequestParam(value = "targetId", required = false) Long targetId) {
        ChatInfoAndRedirectNum chatInfoAndRedirectNum = chatRoomService.getChatRoomList(principalDetail.getEmployeeId(), targetId);

        model.addAttribute("chatRoomList", chatInfoAndRedirectNum.getChatRoomInfos());
        model.addAttribute("initialChatRoomId", chatInfoAndRedirectNum.getRedirectNum() != null ? chatInfoAndRedirectNum.getRedirectNum() : "");
        return "chat_room/chat-room-list";
    }

    /**
     * 채팅방 생성 페이지
     *
     * @param chatRoomCreate 채팅방 생성 정보
     * @param principalDetail 로그인한 사용자 정보
     * @return 리다이렉트 URL
     */
    @PostMapping("/chatrooms/save")
    public String saveChatRoom(@ModelAttribute ChatRoomCreate chatRoomCreate,
                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        chatRoomService.createChatRoom(chatRoomCreate, principalDetail.getEmployeeId());

        return "redirect:/chatrooms";
    }

    /**
     * 그룹채팅 이동
     * @param roomId 선택한 그룹채팅의 번호
     */
    @GetMapping("/group")
    public String groupChatRoom(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam(value = "roomId", required = false) Long roomId) {
        ChatInfoAndRedirectNum chatInfoAndRedirectNum = chatRoomService.getGroupChatList(principalDetail.getEmployeeId(), roomId);

        model.addAttribute("chatRoomList", chatInfoAndRedirectNum.getChatRoomInfos());
        model.addAttribute("initialChatRoomId", chatInfoAndRedirectNum.getRedirectNum() != null ? chatInfoAndRedirectNum.getRedirectNum() : "");
        return "chat_room/chat-room-list";
    }

}
