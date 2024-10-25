package com.kcc.trioffice.domain.schedule.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.domain.schedule.dto.EmployeeSchedules;
import com.kcc.trioffice.domain.schedule.dto.SaveSchedule;
import com.kcc.trioffice.domain.schedule.dto.ScheduleDetail;
import com.kcc.trioffice.domain.schedule.dto.ScheduleMaster;
import com.kcc.trioffice.domain.schedule.mapper.ScheduleMapper;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import com.kcc.trioffice.global.exception.type.ScheduleDeleteException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final ScheduleMapper scheduleMapper;
  private final EmployeeMapper employeeMapper;
  private final JavaMailSender mailSender;

  Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

  public List<EmployeeSchedules> getEmployeeSchedules(String startDate, String endDate,
      PrincipalDetail principalDetail) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String employeeEmail = authentication.getName();
    List<EmployeeSchedules> schdules = scheduleMapper.getEmployeeSchedules(employeeEmail, startDate, endDate);

    for (int i = 0; i < schdules.size(); i++) {
      System.out.println((i + 1) + "번의 일정명은 " + schdules.get(i).getStartedDt());
      // 내 스케줄인지 처리
      if (schdules.get(i).getWriter().equals(principalDetail.getEmployeeId())) {
        schdules.get(i).setIsMySchedule(1);
      } else {
        schdules.get(i).setIsMySchedule(0);
      }
    }
    return schdules;
  }

  @Transactional
  public void saveSchedule(String employeeEmail, SaveSchedule saveSchedule)
      throws BadRequestException, ParseException, MessagingException {
    EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfoFindByEmail(employeeEmail)
        .orElseThrow(() -> new NotFoundException("일치하는 회원이 없습니다."));

    // 문자열을 Timestamp로 변환
    String startedDtStr = saveSchedule.getStartedDt(); // "2024-10-16" 또는 "2024-10-16 17:50"
    String endedDtStr = saveSchedule.getEndedDt(); // "2024-10-16" 또는 "2024-10-16 17:50"

    // 시간을 포함한 형식과 날짜만 있는 형식 모두 처리
    SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    java.util.Date parsedDateStart;
    java.util.Date parsedDateEnd;

    try {
      if (startedDtStr.length() > 10) {
        parsedDateStart = fullDateFormat.parse(startedDtStr); // 시간이 포함된 경우
      } else {
        parsedDateStart = shortDateFormat.parse(startedDtStr); // 날짜만 있는 경우
      }

      if (endedDtStr.length() > 10) {
        parsedDateEnd = fullDateFormat.parse(endedDtStr); // 시간이 포함된 경우
      } else {
        parsedDateEnd = shortDateFormat.parse(endedDtStr); // 날짜만 있는 경우
        // 종료 날짜를 +1일 증가시킴
        Calendar cal = Calendar.getInstance();
        cal.setTime(parsedDateEnd);
        cal.add(Calendar.DATE, 1); // +1일
        parsedDateEnd = cal.getTime();
      }
    } catch (ParseException e) {
      throw new BadRequestException("잘못된 날짜 형식입니다.");
    }

    Timestamp startedDt = new Timestamp(parsedDateStart.getTime());
    Timestamp endedDt = new Timestamp(parsedDateEnd.getTime());

    saveSchedule.setWriter(employeeInfo.getEmployeeId());
    saveSchedule.setModifier(employeeInfo.getEmployeeId());
    saveSchedule.setIsDeleted("0");
    List<String> employeesEmail = new ArrayList<>();

    // mail을 보내기 위한 검사
    if (saveSchedule.getEmailCheck() == 1) {
      List<String> sendEmailEmployeesList = saveSchedule.getEmployeeIds();
      try {
        employeesEmail = employeeMapper.getEmployeeEmailforSend(sendEmailEmployeesList);
      } catch (Exception e) {
        log.info("회원 이메일 가져오기 실패 : " + e);
        throw new BadRequestException("회원 이메일 가져오기 실패 ");
      }
      for (int i = 0; i < employeesEmail.size(); i++) {
        System.out.println("회원들의 이메일 : " + employeesEmail.get(i));
      }

      try {
        Set<String> uniqueEmails = new HashSet<>(employeesEmail);

        for (String oneEmployeeEmail : uniqueEmails) {

          // 이메일 메시지 설정
          MimeMessage message = mailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

          helper.setFrom("noreply@kcc.com");
          helper.setTo(oneEmployeeEmail);
          helper.setSubject("KCC정보통신 | " + saveSchedule.getName() + " 일정 초대 알림");
          String htmlContent = "<div class='email-container' style='max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);'>"
              +
              "<div class='header' style='background-color: #0056b3; color: #ffffff; padding: 20px; text-align: center; border-top-left-radius: 8px; border-top-right-radius: 8px; font-size: 24px;'>"
              +
              "    일정 초대" +
              "</div>" +
              "<div class='content' style='padding: 20px; line-height: 1.6;'>" +
              "    <h2 style='color: #333333;'>안녕하세요,</h2>" +
              "    <p>귀하는 다음 일정에 초대되었습니다:</p>" +
              "    <p><strong>일정 제목:</strong> " + saveSchedule.getName() + "</p>" +
              "    <p><strong>일정 기간:</strong> " + saveSchedule.getStartedDt() + " ~ " + saveSchedule.getEndedDt()
              + "</p>" +
              "    <a href='trioffice.site/notifications' class='button' style='display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #0056b3; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold;'>일정 확인하기</a>"
              +
              "</div>" +
              "<div class='footer' style='text-align: center; padding: 10px; font-size: 12px; color: #999999; border-top: 1px solid #dddddd; margin-top: 20px;'>"
              +
              "    <p>본 알림은 시스템에 의해 자동 생성되었습니다.</p>" +
              "</div>" +
              "</div>";

          helper.setText(htmlContent, true);

          mailSender.send(message);
        }
      } catch (MailException e) {
        e.printStackTrace(); // 예외 처리 로직 추가 가능
      }
    }

    try {
      // saveSchedule 메서드 호출
      scheduleMapper.saveSchedule(saveSchedule, startedDt, endedDt);
      // 이후 saveScheduleInvite 호출
      scheduleMapper.saveScheduleInvite(saveSchedule);
    } catch (Exception e) {
      log.info("에러 발생: ", e);
      throw new BadRequestException("saveSchedule 또는 saveScheduleInvite 중 오류 발생");
    }
  }

  public ScheduleDetail getScheduleDetail(String scheduleId, Long employeeId) {
    // 스케줄의 내용과 생성자
    ScheduleDetail scheduleDetail = scheduleMapper.getScheduleDetail(scheduleId)
        .orElseThrow(() -> new NotFoundException("일정 상세 정보를 가져올 수 없습니다."));

    System.out.println("현재 로그인 객체의 아이디 : " + employeeId + " " + scheduleDetail.getWriter());
    // 스케줄이 본인 것인지 check
    if (employeeId.equals(scheduleDetail.getWriter())) {
      scheduleDetail.setIsMySchedule(1);
      System.out.println("현재 스케줄은 로그인 객체의 것입니다.");
    } else {
      scheduleDetail.setIsMySchedule(0);
    }

    int count = scheduleDetail.getScheduleDetailEmployees().size();

    for (int i = 0; i < count; i++) {
      System.out.println("현재 초대된 회원은 " +
          scheduleDetail.getScheduleDetailEmployees().get(i).getEmployeeName());
    }
    // System.out.println("상세 일정 : " + scheduleDetail.getContents() +
    // scheduleDetail.getWriter());

    // 초대된 회원의 정보
    // scheduleDetail.setScheduleDetailEmployees(scheduleMapper.getInviteEmployee);

    // 주최자
    ScheduleMaster scheduleMaster = scheduleMapper.getScheduleMaster(scheduleDetail.getWriter())
        .orElseThrow(() -> new NotFoundException("주최자를 찾을 수 없습니다."));
    // System.out.println("현재 스케줄을 주최자는 : " + scheduleMaster.getEmployeeName() +
    // scheduleMaster.getDeptName());
    scheduleDetail.setScheduleMaster(scheduleMaster);

    return scheduleDetail;
  }

  @Transactional
  public void deleteSchedule(Long employeeId, Long scheduleId) {

    // 현재 로그인 객체와 일정의 주최자가 같은 사람인지 check
    ScheduleDetail scheduleDetail = scheduleMapper.getScheduleDetail(scheduleId + "")
        .orElseThrow(() -> new NotFoundException("일정 상세 정보를 가져올 수 없습니다."));
    try {
      System.out.println("주최자 삭제 시도 ");
      if (employeeId.equals(scheduleDetail.getWriter())) {
        // 주최자 == 로그인객체
        scheduleMapper.deleteMyScheduleInviteTable(employeeId, scheduleId);
        scheduleMapper.deleteSchedule(employeeId, scheduleId);
      } else {
        // 주최자 != 로그인객체
        scheduleMapper.deleteInvitedSchedule(employeeId, scheduleId);
      }
    } catch (Exception e) {
      // 삭제 실패 시 예외 처리
      throw new ScheduleDeleteException("일정 삭제에 실패했습니다. 원인: " + e.getMessage());
    }

  }

  @Transactional
  public void modifySchedule(SaveSchedule saveSchedule, PrincipalDetail principalDetail)
      throws BadRequestException, MessagingException {

    // 문자열을 Timestamp로 변환
    String startedDtStr = saveSchedule.getStartedDt(); // "2024-10-16" 또는 "2024-10-16 17:50"
    String endedDtStr = saveSchedule.getEndedDt(); // "2024-10-16" 또는 "2024-10-16 17:50"

    // 시간을 포함한 형식과 날짜만 있는 형식 모두 처리
    SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    java.util.Date parsedDateStart;
    java.util.Date parsedDateEnd;

    try {
      if (startedDtStr.length() > 10) {
        parsedDateStart = fullDateFormat.parse(startedDtStr); // 시간이 포함된 경우
      } else {
        parsedDateStart = shortDateFormat.parse(startedDtStr); // 날짜만 있는 경우
      }

      if (endedDtStr.length() > 10) {
        parsedDateEnd = fullDateFormat.parse(endedDtStr); // 시간이 포함된 경우
      } else {
        parsedDateEnd = shortDateFormat.parse(endedDtStr); // 날짜만 있는 경우
        // 종료 날짜를 +1일 증가시킴
        Calendar cal = Calendar.getInstance();
        cal.setTime(parsedDateEnd);
        cal.add(Calendar.DATE, 1); // +1일
        parsedDateEnd = cal.getTime();
      }
    } catch (ParseException e) {
      throw new BadRequestException("잘못된 날짜 형식입니다.");
    }

    Timestamp startedDt = new Timestamp(parsedDateStart.getTime());
    Timestamp endedDt = new Timestamp(parsedDateEnd.getTime());

    saveSchedule.setWriter(principalDetail.getEmployeeId());
    saveSchedule.setModifier(principalDetail.getEmployeeId());
    saveSchedule.setIsDeleted("0");
    List<String> employeesEmail = new ArrayList<>();

    System.out.println(
        "객체에 값이 담기는지 test" + saveSchedule.getContents() + saveSchedule.getName() + saveSchedule.getStartedDt()
            + saveSchedule.getEndedDt()
            + saveSchedule.getEmailCheck()
            + saveSchedule.getScheduleId());

    try {
      scheduleMapper.modifySchedule(saveSchedule, startedDt, endedDt);
      System.out.println(" schedule 수정 성공 ");
      int deleteCount = scheduleMapper.deleteScheduleInvite(saveSchedule.getScheduleId());
      System.out.println("이벤트에서 삭제된 회원의 수 : " + deleteCount);
      scheduleMapper.saveScheduleInvite(saveSchedule);

    } catch (Exception e) {
      log.info("에러 발생: ", e);
      throw new BadRequestException("modifySchedule 또는 saveScheduleInvite 중 오류 발생");
    }

    // mail을 보내기 위한 검사
    if (saveSchedule.getEmailCheck() == 1) {
      List<String> sendEmailEmployeesList = saveSchedule.getEmployeeIds();
      try {
        employeesEmail = employeeMapper.getEmployeeEmailforSend(sendEmailEmployeesList);
      } catch (Exception e) {
        log.info("회원 이메일 가져오기 실패 : " + e);
        throw new BadRequestException("회원 이메일 가져오기 실패 ");
      }
      for (int i = 0; i < employeesEmail.size(); i++) {
        System.out.println("회원들의 이메일 : " + employeesEmail.get(i));
      }

      try {
        Set<String> uniqueEmails = new HashSet<>(employeesEmail);

        for (String oneEmployeeEmail : uniqueEmails) {

          // 이메일 메시지 설정
          MimeMessage message = mailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

          helper.setFrom("noreply@kcc.com");
          helper.setTo(oneEmployeeEmail);
          helper.setSubject("KCC정보통신 | " + saveSchedule.getName() + " 일정 초대 알림");
          String htmlContent = "<div class='email-container' style='max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);'>"
              +
              "<div class='header' style='background-color: #0056b3; color: #ffffff; padding: 20px; text-align: center; border-top-left-radius: 8px; border-top-right-radius: 8px; font-size: 24px;'>"
              +
              "    일정 초대" +
              "</div>" +
              "<div class='content' style='padding: 20px; line-height: 1.6;'>" +
              "    <h2 style='color: #333333;'>안녕하세요,</h2>" +
              "    <p>귀하는 다음 일정에 초대되었습니다:</p>" +
              "    <p><strong>일정 제목:</strong> " + saveSchedule.getName() + "</p>" +
              "    <p><strong>일정 기간:</strong> " + saveSchedule.getStartedDt() + " ~ " + saveSchedule.getEndedDt()
              + "</p>" +
              "    <a href='#' class='button' style='display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #0056b3; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold;'>일정 확인하기</a>"
              +
              "</div>" +
              "<div class='footer' style='text-align: center; padding: 10px; font-size: 12px; color: #999999; border-top: 1px solid #dddddd; margin-top: 20px;'>"
              +
              "    <p>본 알림은 시스템에 의해 자동 생성되었습니다.</p>" +
              "</div>" +
              "</div>";

          helper.setText(htmlContent, true);

          mailSender.send(message);
        }
      } catch (MailException e) {
        e.printStackTrace(); // 예외 처리 로직 추가 가능
      }
    }
  }

}
