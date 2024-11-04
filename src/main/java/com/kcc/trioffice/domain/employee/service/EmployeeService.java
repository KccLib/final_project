package com.kcc.trioffice.domain.employee.service;

import com.kcc.trioffice.domain.common.domain.EmployeeInfoWithDept;
import com.kcc.trioffice.domain.common.mapper.OtherEmployeeMapper;
import com.kcc.trioffice.domain.employee.dto.request.SaveEmployee;
import com.kcc.trioffice.domain.employee.dto.request.SaveFcmToken;
import com.kcc.trioffice.domain.employee.dto.request.UpdateStatus;
import com.kcc.trioffice.domain.employee.dto.response.AdminInfo;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.global.enums.StatusType;
import com.kcc.trioffice.global.exception.type.EmployeeSaveException;
import com.kcc.trioffice.global.exception.type.NotFoundException;

import com.kcc.trioffice.global.image.FilePathUtils;
import com.kcc.trioffice.global.image.S3SaveDir;
import com.kcc.trioffice.global.image.dto.response.S3UploadFile;
import com.kcc.trioffice.global.image.service.S3FileService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.MailException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;
    private final JavaMailSender mailSender;
    private final OtherEmployeeMapper otherEmployeeMapper;
    private final S3FileService s3FileService;

    // 임시비밀번호 생성
    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // example
    }

    public List<SearchEmployee> getEmployeeByCompanyId(Long employeeId) {

        EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfo(employeeId)
                .orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));

        return employeeMapper.getEmployeeByCompanyIdExceptOneSelf(employeeInfo.getCompanyId(), employeeId);
    }

    @Transactional
    public int employeeSave(SaveEmployee saveEmployee) throws EmployeeSaveException {
        // 비밀번호 인코딩
        String password = saveEmployee.getPassword();
        String incodingPassword = passwordEncoder.encode(password);
        saveEmployee.setPassword(incodingPassword);

        // 회원저장
        int isSuccess = employeeMapper.saveEmployee(saveEmployee);

        if (isSuccess == 1) {
            System.out.println("Employee 저장이 성공하였습니다.");
            return isSuccess;
        } else {
            throw new EmployeeSaveException("Employee 저장이 실패하였습니다.");
        }
    }

    @Transactional
    public void checkEmployeeEmail(String email) {
        employeeMapper.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("일치하는 회원의 아이디가 없습니다."));

    }

    @Transactional
    public void temporaryPassword(String email, String externalEmail) throws MessagingException {
        String employeeMail = email;

        String findedExternalEmail = employeeMapper.getExternalEmail(employeeMail).get();

        if (externalEmail.equals(findedExternalEmail)) {
            // 임시비밀번호 발급
            String tmpPassword = generateTempPassword();

            try {
                // 이메일 메시지 설정
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(employeeMail);
                helper.setTo(externalEmail);
                helper.setSubject("KCC정보통신 사내계정 임시비밀번호");
                String htmlContent = "<div style='font-family: Arial, sans-serif; color: #333;'>"
                        + "<h2 style='color: #4CAF50;'>임시 비밀번호</h2></br>"
                        + "<p>요청하신 임시 비밀번호가 생성되었습니다. 아래의 임시 비밀번호를 사용하여 로그인해 주세요:</p>"
                        + "<div style='background-color: #f9f9f9; border: 1px solid #ddd; padding: 10px; margin: 15px 0;'>"
                        + "<strong>임시 비밀번호:</strong> <span style='color: #e74c3c; font-size: 18px;'>" + tmpPassword
                        + "</span>"
                        + "</div>"
                        + "<p>보안을 위해 로그인 후 비밀번호를 반드시 변경해 주시기 바랍니다.</p>"
                        + "<br>"
                        + "<p>감사합니다.</p>"
                        + "</div>";
                helper.setText(htmlContent, true);

                mailSender.send(message);
            } catch (MailException e) {
                e.printStackTrace(); // 예외 처리 로직 추가 가능
            }

            // DB에 담기 위해 incoding
            String incodingPassword = passwordEncoder.encode(tmpPassword);
            employeeMapper.temporaryPassword(incodingPassword, employeeMail);

        } else {
            throw new NotFoundException("등록하신 외부이메일과 다릅니다.");
        }
    }

    public Map<String, Object> getAdminInfo() {
        Map<String, Object> getAdminNameAndPhone = new HashMap<>();

        AdminInfo adminInfo = employeeMapper.getAdminInfo()
                .orElseThrow(() -> new NotFoundException("admin 정보를 찾을 수 없습니다."));
        getAdminNameAndPhone.put("adminName", adminInfo.getName());
        getAdminNameAndPhone.put("adminPhone", adminInfo.getPhoneNum());

        return getAdminNameAndPhone;
    }

    public EmployeeInfo getEmployeeInfo(Long employeeId) {
        return employeeMapper.getEmployeeInfo(employeeId).orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));
    }

    public Optional<List<SearchEmployee>> getAllEmployeesInfo() {
        List<SearchEmployee> employees = employeeMapper.getAllEmployeesInfo();

        if (employees.isEmpty()) {
            throw new NotFoundException("등록된 직원이 없습니다.");
        } else {
            return Optional.of(employees);
        }
    }

    public EmployeeInfo getEmployeeInfoFindByEmail(String email) {

        return employeeMapper.getEmployeeInfoFindByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));
    }

    public EmployeeInfo findById(Long id) {
        return employeeMapper.getEmployeeInfo(id).orElseThrow(() -> new NotFoundException("사원이 없습니다."));
    }

    @Transactional
    public void saveFcmToken(SaveFcmToken saveFcmToken, Long employeeId) {
        employeeMapper.saveFcmToken(employeeId, saveFcmToken.getFcmToken());
    }

    @Transactional
    public void changeEmployeeStatus(Long employeeId, UpdateStatus updateStatus) {
        employeeMapper.changeEmployeeStatus(employeeId, StatusType.toEnum(updateStatus.getStatus()).getValue());
    }

    @Transactional
    public int saveEmployeeFindById(EmployeeInfo employeeInfo) throws EmployeeSaveException {
        // 비밀번호 인코딩
        String password = employeeInfo.getPassword();
        String incodingPassword = passwordEncoder.encode(password);
        employeeInfo.setPassword(incodingPassword);

        // 회원저장
        int isSuccess = employeeMapper.saveEmployeeFindById(employeeInfo);

        if (isSuccess == 1) {
            System.out.println("Employee 등록이 성공하였습니다.");
            return isSuccess;
        } else {
            throw new EmployeeSaveException("Employee 등록이 실패하였습니다.");
        }
    }

    // 모든 포지션을 조회하는 메서드 추가
    public List<String> getAllPositions() {
        return employeeMapper.getAllPositions(); // EmployeeMapper에서 모든 포지션 정보 조회
    }

    public EmployeeInfoWithDept getEmployeeInfoWithDept(Long employeeId) {
        EmployeeInfoWithDept employeeInfoWithDept = otherEmployeeMapper.getEmployeeInfoWithDept(employeeId).orElseThrow(() -> new NotFoundException("회원정보를 조회할 수 없습니다."));

        return  employeeInfoWithDept;
    }

    @Transactional
    public EmployeeInfoWithDept updateEmployeeStatus(int status, Long employeeId) {
        employeeMapper.updateEmployeeStatus(status, employeeId);
        EmployeeInfoWithDept employeeInfoWithDeptForStatus = otherEmployeeMapper.getEmployeeInfoWithDept(employeeId).orElseThrow(() -> new NotFoundException("회원정보를 조회할 수 없습니다."));
        return employeeInfoWithDeptForStatus;
    }

    @Transactional
    public String updateEmployeeProfile(Long employeeId, MultipartFile profile) {
        String profileUrl = "";

        String s3UploadProfilePath = FilePathUtils.createS3UploadFilePath(profile);
        S3UploadFile s3UploadProfile = s3FileService.upload(profile, s3UploadProfilePath, S3SaveDir.PROFILE);

        profileUrl = s3UploadProfile.getFileUrl();
        employeeMapper.updateEmployeeProfile(s3UploadProfile.getFileUrl(), employeeId);

        return profileUrl;
    }

    @Transactional
    public Map<String, String> updateEmployeeStatusMessage(Long employeeId, String message) {

        employeeMapper.updateEmployeeStatusMessage(employeeId, message);

        Map<String, String> employeeStatusMessage = new HashMap<>();
        employeeStatusMessage.put("statusContents", message);
        return employeeStatusMessage;
    }

    public Map<String, Integer> detailPasswordCheck(Long employeeId, String password) {
        EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfo(employeeId).orElseThrow(() -> new NotFoundException("일치하는 회원 정보가 없습니다."));
        String encodedPassword = employeeInfo.getPassword();
        Map<String, Integer> responsePasswordCheck = new HashMap<>();
        //평문 먼저, 암호화 나중에 ***
        if (passwordEncoder.matches(password, encodedPassword)) {
            responsePasswordCheck.put("passwordCheck", 1);
            System.out.println("비밀번호가 같습니다.");
        } else {
            responsePasswordCheck.put("passwordCheck", 2);
            System.out.println("비밀번호가 일치하지 않습니다.");
        }

        return responsePasswordCheck;
    }

    @Transactional
    public boolean modifyEmployee(EmployeeInfo employeeInfo, Long employeeId) {
        int modifyCheck = employeeMapper.modifyEmployee(employeeInfo, employeeId);

        if (modifyCheck == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean changeEmployeePassword(Long employeeId, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        int changePassword = employeeMapper.changeEmployeePassword(employeeId, encodedPassword);

        if(changePassword == 1) {
            return true;
        } else{
            return false;
        }
    }
}
