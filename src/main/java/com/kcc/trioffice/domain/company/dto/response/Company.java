package com.kcc.trioffice.domain.company.dto.response;

import lombok.Data;

@Data
public class Company {
    private Long companyId;
    private String companyName;
    private String writeDt;


    public Company() {}
}
