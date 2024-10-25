package com.kcc.trioffice.domain.notification.controller;

import com.kcc.trioffice.domain.notification.dto.response.NotificationInfo;
import com.kcc.trioffice.domain.notification.service.NotificationService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String getNotification(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                  Model model) {
        List<NotificationInfo> notifications = notificationService.getNotification(principalDetail.getEmployeeId());
        model.addAttribute("notifications", notifications);

        return "notification/notification";
    }
}
