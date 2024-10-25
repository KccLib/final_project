package com.kcc.trioffice.domain.employee.controller;

import com.kcc.trioffice.domain.employee.dto.request.SaveFcmToken;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.domain.employee.service.EmployeeService;

import com.kcc.trioffice.global.auth.PrincipalDetail;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    @GetMapping("/employees/all")
    public ResponseEntity<List<SearchEmployee>> getEmployeesByCompany(
            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ResponseEntity.ok(employeeService
                .getEmployeeByCompanyId(principalDetail.getEmployeeId()));
    }

    @GetMapping("/employees/all/include")
    public ResponseEntity<List<SearchEmployee>> getAllEmployeesInfo() {
        List<SearchEmployee> allEmployeeData = employeeService.getAllEmployeesInfo().get();
        return ResponseEntity.ok(allEmployeeData);
    }

    @GetMapping("/current-employee")
    public ResponseEntity<EmployeeInfo> getCurrentEmployee(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ResponseEntity.ok(employeeService.getEmployeeInfo(principalDetail.getEmployeeId()));
    }
    @GetMapping("/find-password/id")
    public HttpStatus findPasswordCheckId(@RequestParam final String email) {
        System.out.println("요청한 회원 이메일 :" + email);
        employeeService.checkEmployeeEmail(email);
        // String responseEmail = employeeService.checkEmployeeEmail(email);
        // Map<String, Object> response = new HashMap<>();
        // response.put("email", responseEmail);
        return HttpStatus.OK;
    }

    @PostMapping("/find-password/email")
    public HttpStatus passwordChange(@RequestParam final String email, @RequestParam final String externalEmail)
            throws MessagingException {
        System.out.println("요청한 사외 이메일 :" + externalEmail);
        employeeService.temporaryPassword(email, externalEmail);
        return HttpStatus.OK;
    }

    @GetMapping("/find-admin")
    public ResponseEntity<Map<String, Object>> findAdminNameAndPhoneNum() {
        Map<String, Object> adminInfo = employeeService.getAdminInfo();
        return ResponseEntity.ok(adminInfo);
    }

    @PostMapping("/employees/fcm-token")
    public void saveFcmToken(@RequestBody SaveFcmToken saveFcmToken, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        employeeService.saveFcmToken(saveFcmToken, principalDetail.getEmployeeId());
    }

}
