package com.kcc.trioffice.domain.chat_bot.service;

import com.kcc.trioffice.domain.chat_bot.domain.Document;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VectorStoreService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private EmployeeMapper employeeMapper;

    public void addEmployeeInfo(EmployeeInfo employeeInfo) { // 고유한 키 생성
        redisTemplate.opsForValue().set("employeeId", "" + employeeInfo.getEmployeeId());
        redisTemplate.opsForValue().set("deptId", "" + employeeInfo.getDeptId());
        redisTemplate.opsForValue().set("companyId", "" + employeeInfo.getCompanyId());
        redisTemplate.opsForValue().set("email", "" + employeeInfo.getEmail());
        redisTemplate.opsForValue().set("phoneNum", "" + employeeInfo.getPhoneNum());
        redisTemplate.opsForValue().set("externalEmail", "" + employeeInfo.getExternalEmail());
        redisTemplate.opsForValue().set("name", "" + employeeInfo.getName());
        redisTemplate.opsForValue().set("birth", "" + employeeInfo.getBirth());
        redisTemplate.opsForValue().set("profileUrl", "" + employeeInfo.getProfileUrl());
        redisTemplate.opsForValue().set("fax", "" + employeeInfo.getFax());
        redisTemplate.opsForValue().set("location", "" + employeeInfo.getLocation());
        redisTemplate.opsForValue().set("isReceiveNotification", "" + employeeInfo.getIsReceiveNotification());
        redisTemplate.opsForValue().set("position", "" + employeeInfo.getPosition());
        redisTemplate.opsForValue().set("statusMessage", "" + employeeInfo.getStatusMessage());
        redisTemplate.opsForValue().set("position", "" + employeeInfo.getPosition());

    }

    //employee가 했던 질문 redis에 저장하기
    public void add(Map<String, String> vectorInsert) {
        for (Map.Entry<String, String> entry : vectorInsert.entrySet()) {
            String key = entry.getKey(); // 키 가져오기
            String value = entry.getValue(); // 값 가져오기
            redisTemplate.opsForValue().set(key, value); // Redis에 저장
        }
    }

    public List<String> similaritySearch(SearchRequest request) {
        String keyword = request.getQuery(); // 검색할 키워드
        // Redis에서 모든 Document 객체를 가져옵니다.
        List<String> allDocuments = redisTemplate.keys("doc:*").stream()
                .map(key ->  redisTemplate.opsForValue().get(keyword)) // Object를 Document로 캐스팅
                .filter(Objects::nonNull) // null 값 필터링
                .collect(Collectors.toList());

        // 키워드와 일치하는 문서 필터링
        List<String> matchedDocuments = allDocuments.stream()
                .filter(doc -> doc.contains(keyword)) // Document의 내용에서 키워드를 포함하는지 확인
                .collect(Collectors.toList());

        return matchedDocuments; // 유사 문서 목록 반환
    }


}
