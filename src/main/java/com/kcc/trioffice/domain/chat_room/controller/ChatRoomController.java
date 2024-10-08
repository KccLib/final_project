package com.kcc.trioffice.domain.chat_room.controller;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatRoomInfo;
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

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chatrooms")
    public String chatRoomList(Model model,
                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<ChatRoomInfo> chatRoomList = chatRoomService.getChatRoomList(principalDetail.getEmployeeId());

        model.addAttribute("chatRoomList", chatRoomList);
        return "chat_room/chat-room-list";
    }

    @GetMapping("/chatrooms/save")
    public String chatRooms() {
        return "chat_room/chat-room-save";
    }

    @PostMapping("/chatrooms/save")
    public String saveChatRoom(@ModelAttribute ChatRoomCreate chatRoomCreate,
                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        chatRoomService.createChatRoom(chatRoomCreate, principalDetail.getEmployeeId());

        return "redirect:/chatrooms";
    }

}
