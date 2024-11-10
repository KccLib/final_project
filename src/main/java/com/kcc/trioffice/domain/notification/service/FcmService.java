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

    /**
     * 푸시 알림 전송
     *
     * 알림을 보냈는지 확인하는 데에 시간이 오래 걸려비동기로 이루어졌습니다.
     * 추후에 알림 전송이 실패했을 때 exception 잡는 로직이 필요할 것 같습니다.
     *
     * @param sendPushDto 푸시 알림 전송할 데이터
     * @param targetEmployeeId 푸시 알림을 받을 직원 번호
     */
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
            log.info("error: " + e.getMessage());
            throw new ServerException("푸시 알림 전송에 실패하였습니다.");
        }

    }
}
