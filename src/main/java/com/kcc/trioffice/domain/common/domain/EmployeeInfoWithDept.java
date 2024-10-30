package com.kcc.trioffice.domain.common.domain;


import lombok.Data;

@Data
public class EmployeeInfoWithDept {
    private Long employeeId;
    private Long deptId;
    private Long companyId;
    private String email;
    private String password;
    private String phoneNum;
    private String externalEmail;
    private String name;
    private String birth;
    private String profileUrl;
    private String fax;
    private String location;
    private Boolean isReceiveNotification;
    private String position;
    private Long status;
    private String statusMessage;
    private String fcmToken;
    private Long writer;
    private String writeDt;
    private Long modifier;
    private String modifiedDt;
    private String isDeleted;
    private String deptName;
}
