package com.kcc.trioffice.domain.chat_bot.service;

import java.util.HashMap;
import java.util.Map;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatMessage;
import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import com.kcc.trioffice.global.chat_bot.ChatBotConfig;
import com.kcc.trioffice.global.chat_bot.CustomChatBotOptions;
import com.kcc.trioffice.global.enums.ChatType;
import com.kcc.trioffice.global.exception.type.BadRequestException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcc.trioffice.domain.chat_bot.mapper.ChatBotMapper;
import com.kcc.trioffice.global.exception.type.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatBotService {

  private final ChatBotMapper chatBotMapper;
  private final OpenAiChatModel chatModel;
  private final ChatMessage chatMessage = new ChatMessage();
  private final ChatBotConfig  chatBotConfig;

  @Autowired
  private TransactionTemplate transactionTemplate;

  public Flux<String> streamChatBotResponse(String message, PrincipalDetail principalDetail) throws BadRequestException {
      System.out.println("서버로 보낸 메세지  " + message);

      String responseMessage = "";
      // ai 데이터 생성
      try {
//        responseMessage = chatClient.prompt()
//            .user(message)
//            .options(new CustomChatBotOptions("gpt-3.5-turbo", 500))
//            .call()
//            .content();
          responseMessage = chatBotConfig.generatePirateNames(chatModel, message);

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
      ChatRoomCreate chatRoomCreate = new ChatRoomCreate();
      chatRoomCreate.setChatRoomName("chat-bot");
      chatRoomCreate.setWriter(principalDetail.getEmployeeId());

      chatMessage.setMessage(responseMessage);
      chatMessage.setChatId(0L);
      chatMessage.setChatType(ChatType.CHAT_BOT.getValue());
      chatMessage.setSenderId(principalDetail.getEmployeeId());

// 트랜잭션 처리
      transactionTemplate.executeWithoutResult(status -> {
          try {
              int chatBotRoomCount = chatBotMapper.checkChatBotRoom(ChatType.CHAT_BOT.getValue(), principalDetail.getEmployeeId());

              //기존 ChatBotRoom이 없으면 만들어주고 
              if(chatBotRoomCount == 0) {
                  chatBotMapper.saveChatRoom(chatRoomCreate);
                  chatMessage.setRoomId(chatRoomCreate.getChatRoomId());
                  chatBotMapper.saveParticipationEmployee(chatRoomCreate.getChatRoomId(), principalDetail.getEmployeeId());

              } else { //있으면 chatBotRoom의 값만 가져와서 setting
                  Long chatBotRoomNumber = chatBotMapper.employeeRoomNumber(ChatType.CHAT_BOT.getValue(), principalDetail.getEmployeeId());
                  chatMessage.setRoomId(chatBotRoomNumber);
              }
              
              //채팅저장
              chatBotMapper.saveChatMessage(chatMessage);


          } catch (Exception e) {
              log.error("데이터 삽입 중 에러 발생 : " , e);
              status.setRollbackOnly();  // 트랜잭션 롤백
              throw e;
          }
      });





      return  responseFlux;
    }
}
