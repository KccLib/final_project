package com.kcc.trioffice.domain.schedule.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.kcc.trioffice.domain.schedule.dto.EmployeeSchedules;
import com.kcc.trioffice.domain.schedule.dto.SaveSchedule;
import com.kcc.trioffice.domain.schedule.dto.ScheduleDetail;

@Mapper
public interface ScheduleMapper {

  List<EmployeeSchedules> getEmployeeSchedules(String employeeId, String startDate, String endDate);

  int saveSchedule(SaveSchedule saveSchedule, Timestamp startedDt, Timestamp endedDt);

  int saveScheduleInvite(SaveSchedule saveSchedule);

  Optional<ScheduleDetail> getScheduleDetail(String scheduleId);


  int deleteInvitedSchedule(Long employeeId, Long scheduleId);

  int deleteMyScheduleInviteTable(Long employeeId, Long scheduleId);

  int deleteSchedule(Long employeeId, Long scheduleId);

}
