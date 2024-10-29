package com.kcc.trioffice.domain.common.domain;

import com.kcc.trioffice.domain.schedule.dto.ComponentDto;

import lombok.Data;

@Data
public class ChatBot extends ComponentDto {
  private String name;
  private String message;

}
