package com.kcc.trioffice.domain.common.controller;


import com.kcc.trioffice.domain.common.domain.EmployeeInfoWithDept;
import com.kcc.trioffice.domain.common.service.OtherEmployeeService;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OtherEmployeeRestController {

    private final OtherEmployeeService otherEmployeeService;


    @GetMapping("/other/{id}")
    public ResponseEntity<EmployeeInfoWithDept> getOtherEmployeeInfo(@PathVariable Long id) {
        EmployeeInfoWithDept employeeInfo = otherEmployeeService.getOtherEmployeeInfo(id);

        return ResponseEntity.ok(employeeInfo);
    }
}
