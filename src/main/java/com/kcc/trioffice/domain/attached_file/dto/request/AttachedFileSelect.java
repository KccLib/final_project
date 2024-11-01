package com.kcc.trioffice.domain.attached_file.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachedFileSelect {
    private String extensions;
    private Long senderId;
    private String startDate;
    private String endDate;
    private String keyword;
    private Long offset = 0L;
}
