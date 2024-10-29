package com.kcc.trioffice.domain.chat_bot.controller;

import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.trioffice.domain.chat_bot.service.ChatBotService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
@Slf4j
public class ChatBotRestController {

  private final ChatBotService chatBotService;

  @GetMapping(value = "/chat-bot", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> getClientMessages(@RequestParam String clientMessage, @AuthenticationPrincipal PrincipalDetail principalDetail) {
    log.info("받은 메세지: {}", clientMessage);

    Flux<String> responseMessage = chatBotService.streamChatBotResponse(clientMessage, principalDetail);
    //공백 정상 출력
//            .doOnNext(ch -> System.out.println("보내는 메세지 : " + ch + "공백 체크")
    return responseMessage;
  }
}
