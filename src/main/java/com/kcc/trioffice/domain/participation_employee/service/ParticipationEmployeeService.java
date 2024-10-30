package com.kcc.trioffice.domain.participation_employee.service;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatMessage;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatMessageInfo;
import com.kcc.trioffice.domain.chat_room.dto.response.ParticipantEmployeeInfo;
import com.kcc.trioffice.domain.chat_room.mapper.ChatMapper;
import com.kcc.trioffice.domain.chat_room.mapper.ChatRoomMapper;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.domain.participation_employee.dto.response.PtptEmpInfos;
import com.kcc.trioffice.domain.participation_employee.mapper.ParticipationEmployeeMapper;
import com.kcc.trioffice.global.enums.ChatType;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationEmployeeService {

    private final ParticipationEmployeeMapper participationEmployeeMapper;
    private final EmployeeMapper employeeMapper;
    private final ChatMapper chatMapper;
    private final ChatRoomMapper chatRoomMapper;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 채팅방 참여자 조회(이미 참여한 직원들 제외)
     *
     * @param chatroomId 채팅방 번호
     * @param employeeId 로그인한 사용자 번호
     * @return 참여자 목록
     */
    public List<SearchEmployee> getEmployeesExceptParticipants(Long chatroomId, Long employeeId) {

        EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfo(employeeId).orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));

        return participationEmployeeMapper.getEmployeeByChatRoomIdExceptParticipants(employeeInfo.getCompanyId(), chatroomId);
    }

    /**
     * 채팅방 참여자 추가
     *
     * 소켓
     * - 채팅방에 참여자 추가됐다는 이벤트 발생
     * - 채팅 목록에도 추가된 참여자가 보이도록 이벤트 발생
     *
     * @param chatRoomId 채팅방 번호
     * @param employees 추가할 참여자 목록
     * @param employeeId 로그인한 사용자 번호
     */
    @Transactional
    public void addParticipants(Long chatRoomId, List<Long> employees, Long employeeId) {

        List<EmployeeInfo> employeeInfoList = employeeMapper.getEmployeeInfoList(employees);
        List<String> employeeNames = employeeInfoList.stream().map(EmployeeInfo::getName).toList();
        EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfo(employeeId).orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));

        String message =  employeeInfo.getName() + "님이 " + String.join(", ", employeeNames) + "님을 초대하였습니다.";

        for (Long participantEmployeeId : employees) {
            participationEmployeeMapper.saveParticipationEmployee(chatRoomId, participantEmployeeId, employeeId);
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(chatRoomId)
                .senderId(employeeId)
                .message(message)
                .chatType(ChatType.ENTER.getValue())
                .build();
        chatMapper.saveChatMessage(chatMessage);

        chatRoomMapper.updateChatRoomLastMessage(chatMessage.getRoomId(), chatMessage.getChatId());
        List<ParticipantEmployeeInfo> participantEmployeeInfos = participationEmployeeMapper.getParticipantEmployeeInfoByChatRoomId(chatRoomId);

        ChatMessageInfo chatMessageInfo = ChatMessageInfo
                .of(employeeInfo, chatMessage, 0);

        eventPublisher.publishEvent(new PtptEmpInfos(participantEmployeeInfos));
        eventPublisher.publishEvent(chatMessageInfo);
    }
}
