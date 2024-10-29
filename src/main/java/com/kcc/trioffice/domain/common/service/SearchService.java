package com.kcc.trioffice.domain.common.service;


import com.kcc.trioffice.domain.common.domain.SearchChatRoom;
import com.kcc.trioffice.domain.common.mapper.SearchMapper;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;

    public List<SearchEmployee> getEmployeeList(Long employeeId) {
        List<SearchEmployee> employeeList = searchMapper.getAllEmployeesInfo(employeeId);


        return employeeList;
    }

    public List<SearchChatRoom> getSearchChatRoom(Long employeeId) {
        List<SearchChatRoom> searchChatRoomList = searchMapper.getMyChatRooms(employeeId);

        return searchChatRoomList;

    }
}
