package com.kcc.trioffice.domain.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    private String id; // 문서 ID
    private String content; // 문서 내용
    private Map<String, LocalDate> metadata; // 메타데이터

    public Document(String content, Map<String, LocalDate> metadata) {
        this.content = content;
        this.metadata = metadata;
        this.id = generateId(content); // ID 생성 로직
    }

    // ID 생성 로직
    private String generateId(String content) {
        return String.valueOf(content.hashCode()); // 해시 코드로 ID 생성
    }


    public void setMetadata(String date, LocalDate currentDate) {
    }
}