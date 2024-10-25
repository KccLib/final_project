package com.kcc.trioffice.domain.chat_bot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;

import org.springframework.stereotype.Service;

import com.kcc.trioffice.domain.chat_bot.mapper.ChatBotMapper;
import com.kcc.trioffice.global.exception.type.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatBotService {

  private final ChatBotMapper chatBotMapper;
  private final ChatClient chatClient;

  public Flux<String> streamChatBotResponse(String message) {
      System.out.println("서버로 보낸 메세지  " + message);

      String responseMessage = "";
      try {
        responseMessage = chatClient.prompt()
            .user(message)
            .call()
            .content();
      } catch (Exception e) {
        log.info("ai로부터 응답 response 불가" + e);
        throw new NotFoundException("GPT의 응답을 생성할 수 없습니다." + e);
      }

      StringBuilder messageBuilder = new StringBuilder();

      Flux<String> responseFlux = Flux.fromStream(responseMessage.chars()
      .mapToObj(c -> String.valueOf((char) c)))
      .delayElements(Duration.ofMillis(50)) // 100ms마다 한 글자씩 전송
      .doOnNext(ch -> {
          //공백 정상 출력
          System.out.println("현재 문자: '" + ch + "'");
      }); //띄어쓰기 검사하기

      System.out.println("반환된 메세지는 : " +responseMessage);
    return  responseFlux;
    }

}
