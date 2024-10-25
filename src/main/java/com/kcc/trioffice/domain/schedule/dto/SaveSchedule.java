package com.kcc.trioffice.domain.schedule.dto;

import lombok.Data;
import java.util.List;

@Data
public class SaveSchedule extends ComponentDto {
  private Long scheduleId; // 자동 생성된 schedule_id를 저장할 필드
  private String name;
  private String startedDt;
  private String endedDt;
  private String contents;
  private List<Long> employeeIds;
  private int emailCheck;

}
