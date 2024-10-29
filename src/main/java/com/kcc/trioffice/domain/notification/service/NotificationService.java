package com.kcc.trioffice.domain.notification.service;

import com.kcc.trioffice.domain.notification.dto.response.NotificationInfo;
import com.kcc.trioffice.domain.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationMapper notificationMapper;

    public List<NotificationInfo> getNotification(Long employeeId) {
        return notificationMapper.getNotification(employeeId);
    }

}
