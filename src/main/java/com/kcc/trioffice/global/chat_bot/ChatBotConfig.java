package com.kcc.trioffice.global.chat_bot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class ChatBotConfig {
  // String DEFAULT_CHAT_MODEL;
  // static {
  // DEFAULT_CHAT_MODEL = OpenAiApi.ChatModel.GPT_3_5_TURBO.getValue();

  // }

  @Bean
  ChatClient chatClient(ChatClient.Builder builder) {

    // // CustomChatOptions 인스턴스 생성
     CustomChatBotOptions options = new CustomChatBotOptions("gpt-3.5-turbo", 500);
    // // 모델 이름 및 최대 토큰 수 설정
    // ;

    return builder
        .defaultOptions(options)
        .build();
  }
}
