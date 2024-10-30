package com.kcc.trioffice.domain.common.service;


import com.kcc.trioffice.domain.chat_room.mapper.ChatRoomMapper;
import com.kcc.trioffice.domain.common.domain.SearchChatRoom;
import com.kcc.trioffice.domain.common.mapper.SearchMapper;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.domain.participation_employee.mapper.ParticipationEmployeeMapper;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.kcc.trioffice.global.constant.GlobalConstants.DEFAULT_GROUP_IMAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;
    private final ParticipationEmployeeMapper participationEmployeeMapper;
    private final ChatRoomMapper chatRoomMapper;
    private final EmployeeMapper employeeMapper;

    public List<SearchEmployee> getEmployeeList(Long employeeId) {
        List<SearchEmployee> employeeList = searchMapper.getAllEmployeesInfo(employeeId);


        return employeeList;
    }

    public List<SearchChatRoom> getSearchChatRoom(Long employeeId) {
        List<Long> chatRoomList = searchMapper.getMyChatRooms(employeeId);
        List<SearchChatRoom> searchChatRoomList = new ArrayList<>();


        StringBuilder participationEmployees = new StringBuilder();

        chatRoomList.forEach(chatRoomId -> {
            List<Long> employeeIds = searchMapper.participationEmployeefindByChatRoomId(chatRoomId);

            List<String> names = new ArrayList<>();

            employeeIds.forEach(id -> {
                EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfo(id).orElseThrow( () -> new NotFoundException("회원의 아이디를 가져올 수 없습니다."));
                names.add(employeeInfo.getName());
            });

            // 이름 목록을 문자열로 변환하여 participationEmployees에 추가
            if (names != null && !names.isEmpty()) {
                names.forEach(name -> {
                    participationEmployees.append(name).append(", "); // 각 이름 뒤에 쉼표 추가
                });
            }

            if (participationEmployees.length() > 0) {
                participationEmployees.setLength(participationEmployees.length() - 2); // 마지막 쉼표 및 공백 제거
            }
            participationEmployees.append("님과의 채팅");
            searchChatRoomList.add(new SearchChatRoom(participationEmployees.toString(), DEFAULT_GROUP_IMAGE));
            participationEmployees.delete(0, participationEmployees.length());
        });


        return searchChatRoomList;
    }


    public List<SearchEmployee> getChangeEmployeeList(Long employeeId) {
        List<SearchEmployee> employeeList = searchMapper.getAllEmployeesInfo(employeeId);
        
        return employeeList;
    }

    public List<SearchChatRoom> getChangeSearchChatRoom(Long employeeId) {
    }
}
