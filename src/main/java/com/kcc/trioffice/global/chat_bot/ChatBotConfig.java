package com.kcc.trioffice.global.chat_bot;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;


@Configuration
public class ChatBotConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    @Bean
    public OpenAiApi openAiApi() {
        return new OpenAiApi(openAiApiKey);
    }

    @Bean
    public OpenAiChatOptions openAiChatOptions() {
        return OpenAiChatOptions.builder()
                .withModel("gpt-3.5-turbo")
                .withTemperature(0.4)
                .withMaxTokens(2000)
                .withMaxCompletionTokens(1000)
                .build();
    }

    @Bean
    public OpenAiChatModel chatModel(OpenAiApi openAiApi, OpenAiChatOptions openAiChatOptions) {
        return new OpenAiChatModel(openAiApi, openAiChatOptions);
    }

    public String  generatePirateNames(OpenAiChatModel chatModel, String message) {
        Prompt prompt = new Prompt(message);
        ChatResponse response = chatModel.call(prompt);

        // ChatResponse에서 문자열 추출
        String pirateNames = response.getResult().getOutput().getContent(); // getContent() 메서드가 있을 경우
        // 또는 다른 메서드가 있을 수 있으니, 확인해 보세요
        return pirateNames;
    }
}
