package com.kcc.trioffice.domain.employee.controller;

import com.kcc.trioffice.domain.common.domain.EmployeeInfoWithDept;
import com.kcc.trioffice.domain.employee.RequestPassword;
import com.kcc.trioffice.domain.employee.dto.request.SaveFcmToken;
import com.kcc.trioffice.domain.employee.dto.request.UpdateStatus;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.domain.employee.service.EmployeeService;

import com.kcc.trioffice.global.auth.PrincipalDetail;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
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
        log.info("요청한 회원 이메일 : {}", email);
        employeeService.checkEmployeeEmail(email);
        return HttpStatus.OK;
    }

    @PostMapping("/find-password/email")
    public HttpStatus passwordChange(@RequestParam final String email, @RequestParam final String externalEmail)
            throws MessagingException {
        log.info("요청한 사외 이메일 : {}", externalEmail);
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

    @PostMapping("/employees/status")
    public void changeEmployeeStatus(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody UpdateStatus updateStatus) {
        employeeService.changeEmployeeStatus(principalDetail.getEmployeeId(), updateStatus);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeInfo> getEmployee(@PathVariable Long employeeId) {
        EmployeeInfo employeeInfo = employeeService.findById(employeeId);
        return ResponseEntity.ok(employeeInfo);
    }

    @GetMapping("/employees/current-employee")
    public ResponseEntity<EmployeeInfoWithDept> getCurrentEmployeeWithDept(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ResponseEntity.ok(employeeService.getEmployeeInfoWithDept(principalDetail.getEmployeeId()));
    }

    @PutMapping("/employees/status")
    public ResponseEntity<EmployeeInfoWithDept> updateEmployeeStatus(@RequestParam int status, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        EmployeeInfoWithDept employeeInfoWithDept = employeeService.updateEmployeeStatus(status, principalDetail.getEmployeeId());
        return ResponseEntity.ok(employeeInfoWithDept);
    }

    @PutMapping("/employees/profile")
    public ResponseEntity<String> updateEmployeeProfile(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam("profile") MultipartFile profile) {

        String employeeProfileUrl = employeeService.updateEmployeeProfile(principalDetail.getEmployeeId(), profile);
        return ResponseEntity.ok(employeeProfileUrl);
    }

    @PutMapping("/employees/status-message")
    public ResponseEntity<Map<String, String>> updateEmployeeStatusMessage(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam("statusContents") String message){
        Map<String, String> response = employeeService.updateEmployeeStatusMessage(principalDetail.getEmployeeId(), message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/employees/detail/check")
    public ResponseEntity<Map<String, Integer>> detailPasswordCheck(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody RequestPassword requestPassword) {
        System.out.println("받은 password + " + requestPassword.getPassword());

        Map<String, Integer> response = employeeService.detailPasswordCheck(principalDetail.getEmployeeId(), requestPassword.getPassword());
        return ResponseEntity.ok(response);
    }
}
