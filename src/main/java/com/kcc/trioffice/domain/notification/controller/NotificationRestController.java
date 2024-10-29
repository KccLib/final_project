package com.kcc.trioffice.domain.notification.controller;

import com.kcc.trioffice.domain.notification.dto.request.SendPushDto;
import com.kcc.trioffice.domain.notification.service.FcmService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationRestController {

    private final FcmService fcmService;

    @PostMapping("/notifications")
    public void sendNotification(@RequestBody SendPushDto sendPushDto, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        fcmService.sendPush(sendPushDto, principalDetail.getEmployeeId());
    }
}
