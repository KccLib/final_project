package com.kcc.trioffice.global.redis;

import com.kcc.trioffice.domain.notification.dto.request.SendPushDto;
import com.kcc.trioffice.domain.notification.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static com.kcc.trioffice.global.constant.GlobalConstants.MESSAGE_KEY_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final FcmService fcmService;

    public void collectMessage(String receiverId, SendPushDto sendPushDto) {
        String key = MESSAGE_KEY_PREFIX + receiverId;
        log.info("Collecting message for key: {}", key);
        redisTemplate.opsForList().rightPush(key, sendPushDto);

        // 메시지의 TTL 설정 (옵션)
        redisTemplate.expire(key, Duration.ofMinutes(1));
    }

    @Scheduled(fixedRate = 3000)
    public void sendNotifications() {
        // 모든 사용자 메시지 키 가져오기
        Set<String> keys =redisTemplate.keys(MESSAGE_KEY_PREFIX + "*");

        if (keys != null) {
            for (String key : keys) {
                String employeeId = key.replace(MESSAGE_KEY_PREFIX, "");
                List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);

                if (messages != null && !messages.isEmpty()) {
                    // 메시지를 ChatMessage 객체로 캐스팅
                    List<SendPushDto> chatMessages = messages.stream()
                            .map(obj -> (SendPushDto) obj)
                            .toList();

                    // 알림 내용 생성
                    SendPushDto sendPushDto = createNotificationContent(chatMessages);

                    // 알림 발송
                    fcmService.sendPush(sendPushDto, Long.valueOf(employeeId));

                    // 메시지 리스트 삭제
                    redisTemplate.delete(key);
                }
            }
        }
    }

    private SendPushDto createNotificationContent(List<SendPushDto> messages) {
        int messageCount = messages.size();

        if (messageCount == 1) {
            return messages.get(0);
        } else {
            // 가장 최근 메시지의 보낸 사람 정보 사용
            SendPushDto latestMessage = messages.get(messages.size() - 1);
            return SendPushDto.of(latestMessage.getTitle(), "새로운 메시지 " + messageCount + "개가 도착했습니다.", latestMessage.getImage());
        }
    }
}
