package com.kcc.trioffice.domain.chat_room.service;

import com.kcc.trioffice.domain.chat_room.dto.request.ChatMessage;
import com.kcc.trioffice.domain.chat_room.dto.request.ChatRoomCreate;
import com.kcc.trioffice.domain.chat_room.dto.response.*;
import com.kcc.trioffice.domain.chat_room.mapper.ChatMapper;
import com.kcc.trioffice.domain.chat_room.mapper.ChatRoomMapper;
import com.kcc.trioffice.domain.chat_status.mapper.ChatStatusMapper;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.domain.notification.dto.request.SendPushDto;
import com.kcc.trioffice.domain.notification.service.FcmService;
import com.kcc.trioffice.domain.participation_employee.mapper.ParticipationEmployeeMapper;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.service.EmployeeService;
import com.kcc.trioffice.global.enums.ChatType;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomMapper chatRoomMapper;
    private final ParticipationEmployeeMapper participationEmployeeMapper;
    private final EmployeeService employeeService;
    private final ChatMapper chatMapper;
    private final ChatStatusMapper chatStatusMapper;
    private final EmployeeMapper employeeMapper;
    private final FcmService fcmService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createChatRoom(ChatRoomCreate chatRoomCreate, Long employeeId) {
        log.info("chatRoomCreate : {}", chatRoomCreate);
        saveChatRoom(chatRoomCreate, employeeId);
        log.info("chatRoomCreate.getChatRoomId() : {}", chatRoomCreate.getChatRoomId());
        saveParticipantEmployee(chatRoomCreate, employeeId);

        List<EmployeeInfo> employeeInfoList = employeeMapper.getEmployeeInfoList(chatRoomCreate.getEmployees());
        List<String> employeeNames = employeeInfoList.stream().map(EmployeeInfo::getName).toList();
        EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfo(employeeId).orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));
        String message = employeeInfo.getName() + "님이 " + String.join(", ", employeeNames) + "님을 초대하였습니다.";

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(chatRoomCreate.getChatRoomId())
                .senderId(employeeId)
                .message(message)
                .chatType(ChatType.ENTER.getValue())
                .build();
        chatMapper.saveChatMessage(chatMessage);
        chatRoomMapper.updateChatRoomLastMessage(chatMessage.getRoomId(), chatMessage.getChatId());
        sendChatMessageFcm(chatRoomCreate.getChatRoomId(), chatRoomCreate.getChatRoomName(), employeeInfo.getProfileUrl(), message);

    }

    private void saveChatRoom(ChatRoomCreate chatRoomCreate, Long employeeId) {
        int result = chatRoomMapper.saveChatRoom(chatRoomCreate, employeeId);
        if (result == 0) {
            throw new RuntimeException("채팅방 생성에 실패하였습니다.");
        }
    }

    private void saveParticipantEmployee(ChatRoomCreate chatRoomCreate, Long employeeId) {
        List<Long> employees = chatRoomCreate.getEmployees();
        for (Long participantEmployeeId : employees) {
            participationEmployeeMapper.saveParticipationEmployee(chatRoomCreate.getChatRoomId(), participantEmployeeId, employeeId);
        }
        participationEmployeeMapper.saveParticipationEmployee(chatRoomCreate.getChatRoomId(), employeeId, employeeId);
    }

    public List<ChatRoomInfo> getChatRoomList(Long employeeId) {
        List<ChatRoomInfo> chatRoomInfoList = chatRoomMapper.getChatRoomListByEmployeeId(employeeId);

        setChatRoomNameAndProfile(employeeId, chatRoomInfoList);

        return chatRoomInfoList;
    }


    private void setChatRoomNameAndProfile(Long employeeId, List<ChatRoomInfo> chatRoomInfoList) {
        for (ChatRoomInfo chatRoomInfo : chatRoomInfoList) {
            log.info("chatRoomInfo : {}", chatRoomInfo);
            List<EmployeeInfo> employeeInfos = participationEmployeeMapper.getEmployeeByChatRoomIdExceptOneSelf(chatRoomInfo.getChatRoomId(), employeeId);

            if (chatRoomInfo.getParticipantCount() <= 2) {
                EmployeeInfo employeeInfo = employeeInfos.get(0);
                chatRoomInfo.setChatRoomProfileImageUrl(employeeInfo.getProfileUrl());
                chatRoomInfo.setEmployeeStatus(employeeInfo.getStatus());
                if (chatRoomInfo.getChatRoomName() == null) {
                    chatRoomInfo.setChatRoomName(employeeInfo.getName());
                }
            } else {
                chatRoomInfo.setChatRoomProfileImageUrl("https://us.123rf.com/450wm/siamimages/siamimages1703/siamimages170303374/74232239-%EA%B7%B8%EB%A3%B9-%EC%82%AC%EB%9E%8C-%EC%95%84%EC%9D%B4%EC%BD%98.jpg");
                if (chatRoomInfo.getChatRoomName() == null) {
                    List<String> employeeNames = employeeInfos.stream().map(EmployeeInfo::getName).toList();
                    String chatRoomName = String.join(", ", employeeNames);
                    chatRoomInfo.setChatRoomName(chatRoomName);
                }
            }
            if (chatRoomInfo.getLastMessage() == null) {
                chatRoomInfo.setLastMessage("대화방이 생성되었습니다.");
            }
            log.info("chatRoomInfo : {}", chatRoomInfo);

        }
    }

    public ChatRoomDetailInfo getChatRoomDetailInfo(Long chatRoomId, Long employeeId, int limit, int offset) {
        ChatRoomDetailInfo chatRoomDetailInfo = chatRoomMapper.getChatRoomInfo(chatRoomId, employeeId)
                .orElseThrow(() -> new NotFoundException("채팅방이 존재하지 않습니다."));
        List<EmployeeInfo> employeeInfos = participationEmployeeMapper.getEmployeeByChatRoomIdExceptOneSelf(chatRoomId, employeeId);

        List<ChatInfo> chatInfos = chatRoomMapper.getChatInfoByPage(chatRoomId, employeeId, chatRoomDetailInfo.getParticipantCount(), limit, offset);

        chatInfos.forEach(c -> {
            if (c.getIsDeleted()) {
                c.setChatContents("삭제된 메시지입니다.");
                c.setChatType(ChatType.CHAT.getValue());
            }
        });

        chatRoomDetailInfo.setChatInfoList(chatInfos);

        if (chatRoomDetailInfo.getParticipantCount() <= 2) {
            EmployeeInfo employeeInfo = employeeInfos.get(0);
            chatRoomDetailInfo.setChatRoomProfileImageUrl(employeeInfo.getProfileUrl());
            chatRoomDetailInfo.setEmployeeStatus(employeeInfo.getStatus());
            if (chatRoomDetailInfo.getChatRoomName() == null) {
                chatRoomDetailInfo.setChatRoomName(employeeInfo.getName());
            }
        } else {
            chatRoomDetailInfo.setChatRoomProfileImageUrl("https://us.123rf.com/450wm/siamimages/siamimages1703/siamimages170303374/74232239-%EA%B7%B8%EB%A3%B9-%EC%82%AC%EB%9E%8C-%EC%95%84%EC%9D%B4%EC%BD%98.jpg");
            List<String> employeeNames = employeeInfos.stream().map(EmployeeInfo::getName).toList();
            if (chatRoomDetailInfo.getChatRoomName() == null) {
                String chatRoomName = String.join(", ", employeeNames);
                chatRoomDetailInfo.setChatRoomName(chatRoomName);
            }
        }

        log.info("chatRoomDetailInfo : {}", chatRoomDetailInfo);
        return chatRoomDetailInfo;
    }

    @Transactional
    public ChatMessageAndParticipants saveChatMessage(ChatMessage chatMessage) {
        log.info("chatMessage : {}", chatMessage);
        EmployeeInfo senderEmpInfo = employeeService.getEmployeeInfo(chatMessage.getSenderId());
        chatMapper.saveChatMessage(chatMessage);
        chatRoomMapper.updateChatRoomLastMessage(chatMessage.getRoomId(), chatMessage.getChatId());

        //채팅 참여자 조회
        List<ParticipantEmployeeInfo> participantEmployeeInfos = participationEmployeeMapper.getParticipantEmployeeInfoByChatRoomId(chatMessage.getRoomId());

        int unreadCount = getUnreadCountAndCreateChatStatus(chatMessage, participantEmployeeInfos);



        String chatRoomName = handleChatRoomName(chatMessage.getRoomId(), chatMessage.getSenderId());
        String chatProfileImageUrl = senderEmpInfo.getProfileUrl();

        sendChatMessageFcm(chatMessage.getRoomId(), chatRoomName, chatProfileImageUrl, chatMessage.getMessage());

        EmployeeInfo employeeInfo = employeeService.getEmployeeInfo(chatMessage.getSenderId());

        ChatMessageInfo chatMessageInfo = ChatMessageInfo.of(employeeInfo, chatMessage, unreadCount);
        return new ChatMessageAndParticipants(chatMessageInfo, participantEmployeeInfos);
    }

    private int getUnreadCountAndCreateChatStatus(ChatMessage chatMessage, List<ParticipantEmployeeInfo> participantEmployeeInfos) {
        int unreadCount = 0;

        for (ParticipantEmployeeInfo participantEmployeeInfo : participantEmployeeInfos) {
            if (participantEmployeeInfo.getEmployeeId().equals(chatMessage.getSenderId()) || participantEmployeeInfo.getIsEntered()) {
                chatStatusMapper.saveChatStatusRead(chatMessage.getRoomId(), chatMessage.getChatId(), participantEmployeeInfo.getEmployeeId(), chatMessage.getSenderId());
            } else {
                unreadCount++;
                chatStatusMapper.saveChatStatus(chatMessage.getRoomId(), chatMessage.getChatId(), participantEmployeeInfo.getEmployeeId(), chatMessage.getSenderId());
            }
        }
        return unreadCount;
    }

    public void sendChatMessageFcm(Long chatRoomId, String chatRoomName, String chatProfileImageUrl, String message) {
        List<EmployeeInfo> employeeInfos = participationEmployeeMapper.getFcmTokenByChatRoomId(chatRoomId);
        employeeInfos.forEach(e -> {
            log.info("e : {}", e.getEmployeeId());
            fcmService.sendPush(SendPushDto.of(chatRoomName, message, chatProfileImageUrl), e.getEmployeeId());
        });

    }

    public String handleChatRoomName(Long chatRoomId, Long employeeId) {
        ChatRoomDetailInfo chatRoomDetailInfo = chatRoomMapper.getChatRoomInfo(chatRoomId, employeeId)
                .orElseThrow(() -> new NotFoundException("채팅방이 존재하지 않습니다."));
        List<EmployeeInfo> ptptEmpInfoByChatIdExceptOneself = participationEmployeeMapper
                .getPtptEmpInfoByChatIdExceptOneself(chatRoomId, employeeId);
        if (chatRoomDetailInfo.getChatRoomName() == null) {
            List<String> names = ptptEmpInfoByChatIdExceptOneself.stream().map(EmployeeInfo::getName).toList();
            return String.join(", ", names);
        }

        return chatRoomDetailInfo.getChatRoomName();
    }

    public List<EmployeeInfo> getChatRoomEmployees(Long chatRoomId) {
        return participationEmployeeMapper.getEmployeeInfoByChatRoomId(chatRoomId);
    }


    @Transactional
    public ChatMessageInfoAndPtptEmp deleteChatRoom(Long chatRoomId, Long employeeId) {
        participationEmployeeMapper.deleteParticipationEmployee(chatRoomId, employeeId);

        EmployeeInfo employeeInfo = employeeService.getEmployeeInfo(employeeId);
        String message = employeeInfo.getName() + "님이 채팅방을 나갔습니다.";

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(chatRoomId)
                .senderId(employeeId)
                .message(message)
                .chatType(ChatType.QUIT.getValue())
                .build();

        chatMapper.saveChatMessage(chatMessage);
        chatRoomMapper.updateChatRoomLastMessage(chatMessage.getRoomId(), chatMessage.getChatId());

        ChatMessageInfo chatMessageInfo = ChatMessageInfo.builder()
                .chatTime(LocalDateTime.now())
                .chatContents(message)
                .chatId(chatMessage.getChatId())
                .senderName(employeeInfo.getName())
                .senderId(employeeId)
                .chatType(ChatType.QUIT.toString())
                .roomId(chatRoomId)
                .build();


        List<ParticipantEmployeeInfo> participantEmployeeInfos = participationEmployeeMapper.getParticipantEmployeeInfoByChatRoomId(chatRoomId);

        return new ChatMessageInfoAndPtptEmp(participantEmployeeInfos, chatMessageInfo);
    }

    @Transactional
    public void deleteChat(Long chatId, Long employeeId) {
        ChatDetailInfo chatDetailInfo = chatMapper
                .getChatDetailInfo(chatId).orElseThrow(() -> new NotFoundException("채팅이 존재하지 않습니다."));
        if (chatDetailInfo.getSenderId().equals(employeeId)) {
            chatMapper.deleteChatMessage(chatId, employeeId);
        } else {
            throw new RuntimeException("채팅 삭제 권한이 없습니다.");
        }

        ChatDelete chatDelete = new ChatDelete(chatDetailInfo.getChatroomId(), "DELETE", chatDetailInfo.getSenderId(), chatId);

        eventPublisher.publishEvent(chatDelete);
    }

    @Transactional
    public void favoriteChatRoom(Long chatRoomId, Long employeeId) {

        ParticipantEmployeeInfo participantEmployeeInfo = participationEmployeeMapper
                .getPtptptEmpInfo(chatRoomId, employeeId).orElseThrow(() -> new NotFoundException("참여자가 아닙니다."));
        if (participantEmployeeInfo.getIsFavorited()) {
            participationEmployeeMapper.favoriteChatRoom(chatRoomId, employeeId, false);
        } else {
            participationEmployeeMapper.favoriteChatRoom(chatRoomId, employeeId, true);
        }
    }

    public List<ChatRoomInfo> getFavoriteChatRoom(Long employeeId) {
        List<ChatRoomInfo> favoriteChatRooms = chatRoomMapper.getFavoriteChatRooms(employeeId);
        setChatRoomNameAndProfile(employeeId, favoriteChatRooms);
        return favoriteChatRooms;
    }

}
