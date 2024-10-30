package com.kcc.trioffice.domain.participation_employee.controller;

import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.domain.participation_employee.dto.request.AddParticipants;
import com.kcc.trioffice.domain.participation_employee.service.ParticipationEmployeeService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chatrooms/")
public class ParticipationEmployeeRestController {

    private final ParticipationEmployeeService participationEmployeeService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 채팅방 참여자 조회(이미 참여한 직원들 제외)
     *
     * @param chatRoomId 채팅방 번호
     * @param principalDetail 로그인한 사용자 정보
     * @return
     */
    @GetMapping("/{chatRoomId}/except-participants")
    public ResponseEntity<List<SearchEmployee>> getEmployeesExceptParticipants(@PathVariable Long chatRoomId,
                                                                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ResponseEntity
                .ok(participationEmployeeService
                        .getEmployeesExceptParticipants(chatRoomId, principalDetail.getEmployeeId()));
    }

    /**
     * 채팅방 참여자 추가
     *
     * @param chatRoomId 채팅방 번호
     * @param addParticipants 추가할 참여자 정보
     * @param principalDetail 로그인한 사용자 정보
     */
    @PostMapping("/{chatRoomId}/participants")
    public void addChatRoomEmployee(@PathVariable Long chatRoomId,
                                    @RequestBody AddParticipants addParticipants,
                                    @AuthenticationPrincipal PrincipalDetail principalDetail) {

        participationEmployeeService.addParticipants(chatRoomId, addParticipants.getEmployees(), principalDetail.getEmployeeId());
    }

}
