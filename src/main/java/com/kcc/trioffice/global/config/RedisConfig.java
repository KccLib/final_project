package com.kcc.trioffice.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key Serializer
        template.setKeySerializer(new StringRedisSerializer());

        // Value Serializer
        // GenericJackson2JsonRedisSerializer를 사용하여 객체를 JSON 형태로 직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
