package com.kcc.trioffice.domain.common.domain;


import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.domain.schedule.dto.ComponentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseSearch extends ComponentDto {

    private List<SearchChatRoom> searchChatRoomList;
    private List<SearchEmployee> searchEmployeeList;
}
