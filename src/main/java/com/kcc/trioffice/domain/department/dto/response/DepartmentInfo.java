package com.kcc.trioffice.domain.department.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentInfo {

    private Long deptId;
    private String name;
    private Long upperDeptId;
    private Long deptOrder;

}
