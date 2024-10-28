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

    @GetMapping("/{chatRoomId}/except-participants")
    public ResponseEntity<List<SearchEmployee>> getEmployeesExceptParticipants(@PathVariable Long chatRoomId,
                                                                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ResponseEntity
                .ok(participationEmployeeService
                        .getEmployeesExceptParticipants(chatRoomId, principalDetail.getEmployeeId()));
    }

    @PostMapping("/{chatRoomId}/participants")
    public void addChatRoomEmployee(@PathVariable Long chatRoomId,
                                    @RequestBody AddParticipants addParticipants,
                                    @AuthenticationPrincipal PrincipalDetail principalDetail) {

        participationEmployeeService.addParticipants(chatRoomId, addParticipants.getEmployees(), principalDetail.getEmployeeId());
    }

}
