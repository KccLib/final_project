package com.kcc.trioffice.domain.common.service;

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

    public void addEmployeeInfo(EmployeeInfo employeeInfo) {

        String employeeKey = "employee:" + employeeInfo.getEmployeeId(); // 고유한 키 생성

        // Redis에 employeeId 키가 존재하는지 확인
        if (redisTemplate.hasKey(employeeKey)) {
            System.out.println("이미 존재하는 employeeId입니다. 저장을 중단합니다.");
            return; // 키가 존재하면 저장하지 않음
        }

        redisTemplate.opsForValue().set("employeeId", "" + employeeInfo.getEmployeeId());
        redisTemplate.opsForValue().set("부서", "" + employeeInfo.getDeptId());
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

    public void defaultButton(String message) {

        if (redisTemplate.hasKey("doc:"+message)) {
            System.out.println("이미 존재하는 defaultButton Message. 저장을 중단합니다.");
            return;
        }
        String button1 = "doc:자주하는 질문";
        String button2 = "doc:신규기능 활용 세미나\uD83D\uDE0A";
        String button3 = "doc:답변을 잘받는 TIP❗";
        String button4 = "doc:✅ 코드 컨벤션 검사";


        redisTemplate.opsForValue().set(button1, "https://care.daouoffice.co.kr/hc/ko/sections/24482900933913-%EA%B7%B8%EB%A3%B9%EC%9B%A8%EC%96%B4FAQ 여기에서 자주하는 질문 10개를 뽑아서 보여줘");
        redisTemplate.opsForValue().set(button2, "https://main.kcc.co.kr/html/e-book/ecatalog5.php?Dir=50&catimage=에서 KCC정보통신의 신간호를 보고 내용 요약해줘 \n" +
                "10줄로 답변해주고\n" +
                "무슨 링크인지 설명하지말고 각 페이지별로 뭐가 있는지 설명해줘 \n" +
                "한글로 답변해줘 \n" +
                "그리고 상세 링크를 마지막에 줘");
        redisTemplate.opsForValue().set(button3, "간결하고 구체적으로 알려줘");
        redisTemplate.opsForValue().set(button4, "1. 변수와 함수 이름은 카멜 케이스(camelCase)로 작성한다."+
                "2. 상수는 모두 대문자로 작성하며, 단어 사이는 언더스코어(_)로 구분한다."+
                "3. 들여쓰기는 2칸 또는 4칸을 사용하여 코드의 가독성을 유지한다."+
                "4. 문자열에는 일관된 따옴표(single quotes '' or double quotes \"\")를 사용한다."+
                "5. 불필요한 전역 변수 선언을 피하고, const와 let을 사용하여 변수 스코프를 명확히 한다.");
    }

    /**
     *
     * 시간 정보가 없는 질의 시
     * @param vectorInsert
     */

    //employee가 했던 질문 redis에 저장하기
    public void add(Map<String, String> vectorInsert) {
        String key = null;
        for (Map.Entry<String, String> entry : vectorInsert.entrySet()) {
            key = entry.getKey();
            String value = entry.getValue(); // 값 가져오기
            redisTemplate.opsForValue().set("doc:" + key, value); // Redis에 저장
        }

        defaultButton(key);
    }

    public List<String> similaritySearch(SearchRequest request) {
        String keyword = request.getQuery(); // 검색할 키워드
        // doc:*로 시작하는 모든 키 가져오기
        List<String> allKeys = redisTemplate.keys("doc:*").stream()
                .filter(Objects::nonNull)
                .toList();

        System.out.println("redis에 담겨있는 keys count: "+allKeys.size());

        // 모든 문서 값을 가져오기
        List<String> allDocuments = allKeys.stream()
                .map(redisTemplate.opsForValue()::get) // 실제 문서 값을 가져옴
                .filter(Objects::nonNull) // null 값 필터링
                .toList();

        // 키워드와 일치하는 문서 필터링
        List<String> matchedDocuments = allKeys.stream()
                .filter(doc -> doc.contains(keyword)) // Document의 내용에서 키워드를 포함하는지 확인
                .map(redisTemplate.opsForValue()::get)
                .collect(Collectors.toList());

        matchedDocuments.forEach( System.out::println);

        return matchedDocuments; // 유사 문서 목록 반환
    }



}
