package com.kcc.trioffice.domain.participation_employee.mapper;

import com.kcc.trioffice.domain.chat_room.dto.response.ParticipantEmployeeInfo;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ParticipationEmployeeMapper {

    int saveParticipationEmployee(Long chatRoomId, Long employeeId, Long writer);

    List<EmployeeInfo> getEmployeeByChatRoomIdExceptOneSelf(Long chatRoomId, Long employeeId);

    List<ParticipantEmployeeInfo> getParticipantEmployeeInfoByChatRoomId(Long chatRoomId);

    List<EmployeeInfo> getEmployeeInfoByChatRoomId(Long chatRoomId);

    List<SearchEmployee> getEmployeeByChatRoomIdExceptParticipants(Long companyId, Long chatRoomId);

    int deleteParticipationEmployee(Long chatRoomId, Long employeeId);

    int updateIsEntered(Long chatRoomId, Long employeeId, boolean isEntered);

    int disconnectAllChatRoom(Long employeeId);

    List<EmployeeInfo> getPtptEmpInfoByChatIdExceptOneself(Long chatRoomId, Long employeeId);

    List<EmployeeInfo> getFcmTokenByChatRoomId(Long chatRoomId);

    Optional<ParticipantEmployeeInfo> getPtptptEmpInfo(Long chatRoomId, Long employeeId);

    int favoriteChatRoom(Long chatRoomId, Long employeeId, boolean isFavorited);
}
