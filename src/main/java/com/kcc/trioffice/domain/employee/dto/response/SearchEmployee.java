package com.kcc.trioffice.domain.employee.dto.response;

import lombok.Data;

@Data
public class SearchEmployee {

    private Long id;
    private String name;
    private String deptName;
    private Long deptId;
    private String position;
    private String profileURL;
}
