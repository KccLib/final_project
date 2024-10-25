package com.kcc.trioffice.domain.schedule.controller;

import org.springframework.web.bind.annotation.*;

import com.kcc.trioffice.domain.schedule.dto.EmployeeSchedules;
import com.kcc.trioffice.domain.schedule.dto.SaveSchedule;
import com.kcc.trioffice.domain.schedule.dto.ScheduleDetail;
import com.kcc.trioffice.domain.schedule.service.ScheduleService;
import com.kcc.trioffice.global.auth.PrincipalDetail;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleRestController {

    private final ScheduleService scheduleService;

    @GetMapping("/schedules/calendar")
    public ResponseEntity<List<EmployeeSchedules>> getEmployeeSchedules(@RequestParam String startDate,
        @RequestParam String endDate, @AuthenticationPrincipal PrincipalDetail principalDetail) {

      System.out.println("request Date : " + startDate + endDate);
      List<EmployeeSchedules> schedules = scheduleService.getEmployeeSchedules(startDate, endDate, principalDetail);
      return ResponseEntity.ok(schedules);
    }

    @GetMapping("/schedules/detail")
    public ResponseEntity<ScheduleDetail> getScheduleDetail(@AuthenticationPrincipal PrincipalDetail principal,
        @RequestParam String scheduleId) {
      ScheduleDetail scheduleDetail = scheduleService.getScheduleDetail(scheduleId, principal.getEmployeeId());
      return ResponseEntity.ok(scheduleDetail);
    }

    @PatchMapping("/schedules/modify")
    public ResponseEntity<?> modifySchedule(@RequestBody SaveSchedule saveSchedule,
        @AuthenticationPrincipal PrincipalDetail principalDetail)
        throws BadRequestException, MessagingException {
      System.out.println("일정 수정 요청");
      scheduleService.modifySchedule(saveSchedule, principalDetail);
      return ResponseEntity.ok("수정성공");
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@AuthenticationPrincipal PrincipalDetail principalDetail,
        @PathVariable Long scheduleId) {
      System.out.println("일정 삭제 요청");
      scheduleService.deleteSchedule(principalDetail.getEmployeeId(), scheduleId);
      return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/schedules/{scheduleId}/approve")
    public void approveSchedule(@AuthenticationPrincipal PrincipalDetail principalDetail,
        @PathVariable Long scheduleId) {
        scheduleService.approveSchedule(principalDetail.getEmployeeId(), scheduleId);
    }

    @PostMapping("/schedules/{scheduleId}/reject")
    public void rejectSchedule(@AuthenticationPrincipal PrincipalDetail principalDetail,
        @PathVariable Long scheduleId) {
        scheduleService.rejectSchedule(principalDetail.getEmployeeId(), scheduleId);
    }

}
