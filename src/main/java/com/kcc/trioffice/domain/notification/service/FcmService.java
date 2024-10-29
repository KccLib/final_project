package com.kcc.trioffice.domain.notification.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.domain.notification.dto.request.SendPushDto;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import com.kcc.trioffice.global.exception.type.ServerException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FcmService {

    private final EmployeeMapper employeeMapper;

    @PostConstruct
    public void fcmInitialize() throws IOException {
        ClassPathResource key = new ClassPathResource("static/firebase-key.json");

        try (InputStream serviceAccount = key.getInputStream()) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("*********************")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }

    @Transactional
    @Async(value = "AsyncBean")
    public void sendPush(SendPushDto sendPushDto, Long targetEmployeeId) {
        EmployeeInfo employeeInfo = employeeMapper
                .getEmployeeInfo(targetEmployeeId).orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));

        // 메시지 객체 생성
        try {
            Message message = Message.builder()
                    .putData("title", sendPushDto.getTitle())
                    .putData("body", sendPushDto.getContent())
                    .putData("profileUrl", sendPushDto.getImage())
                    .setToken(employeeInfo.getFcmToken())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            throw new ServerException("푸시 알림 전송에 실패하였습니다.");
        }

    }
}
