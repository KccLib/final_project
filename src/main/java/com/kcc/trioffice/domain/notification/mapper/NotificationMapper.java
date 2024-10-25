package com.kcc.trioffice.domain.notification.mapper;

import com.kcc.trioffice.domain.notification.dto.response.NotificationInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {

    int saveNotification(Long employeeId, Long relatedId, Long type, String title, String contents, Long writer);
    List<NotificationInfo> getNotification(Long employeeId);
}
