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

    /**
     * 알림 조회
     *
     * @param employeeId 직원 번호
     * @return 알림 목록
     */
    public List<NotificationInfo> getNotification(Long employeeId) {
        return notificationMapper.getNotification(employeeId);
    }

}
