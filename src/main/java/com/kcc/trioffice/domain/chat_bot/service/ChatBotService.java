package com.kcc.trioffice.domain.chat_bot.service;

import java.util.HashMap;
import java.util.Map;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatMessage;
import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import com.kcc.trioffice.global.enums.ChatType;
import com.kcc.trioffice.global.exception.type.BadRequestException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;

import org.springframework.stereotype.Service;

import com.kcc.trioffice.domain.chat_bot.mapper.ChatBotMapper;
import com.kcc.trioffice.global.exception.type.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatBotService {

  private final ChatBotMapper chatBotMapper;
  private final ChatClient chatClient;

  @Transactional
  public Flux<String> streamChatBotResponse(String message, PrincipalDetail principalDetail) throws BadRequestException {
      System.out.println("서버로 보낸 메세지  " + message);

      String responseMessage = "";
      // ai 데이터 생성
      try {
        responseMessage = chatClient.prompt()
            .user(message)
            .call()
            .content();
      } catch (Exception e) {
        log.info("ai로부터 응답 response 불가" + e);
        throw new NotFoundException("GPT의 응답을 생성할 수 없습니다." + e);
      }

      Flux<String> responseFlux = Flux.fromStream(responseMessage.chars()
      .mapToObj(c -> String.valueOf((char) c)))
      .delayElements(Duration.ofMillis(50)) // 100ms마다 한 글자씩 전송
      .doOnNext(ch -> {
          //공백 정상 출력
//          System.out.println("현재 문자: '" + ch + "'");
      }); //띄어쓰기 검사하기

      System.out.println("반환된 메세지는 : " +responseMessage);
      ChatRoomCreate chatRoomCreate = new ChatRoomCreate(null, null, null);
      ChatMessage chatMessage = new ChatMessage(
              chatRoomCreate.getChatRoomId(), principalDetail.getEmployeeId(),  responseMessage, ChatType.CHAT_BOT.getValue(), 0L);

      chatRoomCreate.setChatRoomName("chat-bot");
      chatRoomCreate.setChatRoomId(principalDetail.getEmployeeId());

      try{
          chatBotMapper.saveChatRoom(chatRoomCreate);
          chatBotMapper.saveParticipationEmployee(chatRoomCreate.getChatRoomId(), principalDetail.getEmployeeId());
          chatBotMapper.saveChatMessage(chatMessage);
      }catch (Exception e ) {
          log.error("chatRoomCreate 관련 insert 중 에러발생 : "+ e);
      }

      try {
          chatBotMapper.saveParticipationEmployee(chatRoomCreate.getChatRoomId(), principalDetail.getEmployeeId());

      }catch (Exception e) {
          log.error("saveParticipationEmployee 관련 insert 중 에러발생 : "+ e);

      }

      try {
          chatBotMapper.saveChatMessage(chatMessage);
      } catch (Exception e) {
          log.error("saveParticipationEmployee 관련 insert 중 에러발생 : "+ e);

      }
      return  responseFlux;
    }

}
