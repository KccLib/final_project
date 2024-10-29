package com.kcc.trioffice.domain.schedule.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ScheduleDetail implements Serializable {
  // 사원의 이름, 부서, 참석여부
  private List<ScheduleDetailEmployees> scheduleDetailEmployees;
  private String name;
  private String contents;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private Timestamp startedDt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private Timestamp endedDt;
  private Long writer;
  private int isMySchedule;
  private ScheduleMaster scheduleMaster;
}
