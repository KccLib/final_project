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
import com.kcc.trioffice.domain.participation_employee.dto.response.PtptEmpInfos;
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

import static com.kcc.trioffice.global.constant.GlobalConstants.*;

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


    /**
     * 채팅방 생성
     * 채팅방 생성, 채팅방 참여자 저장, 초대 메시지 전송
     *
     * 알림
     * - 채팅방 생성 시 채팅방에 초대된 사람들에게 채팅방 생성 이벤트 전송
     *
     * 소켓
     * - 채팅방 생성 시 채팅방에 초대된 사람들에게 채팅방 생성 이벤트 전송
     * @param chatRoomCreate 채팅방 생성 정보
     * @param employeeId 초대한 직원 아이디
     */
    @Transactional
    public void createChatRoom(ChatRoomCreate chatRoomCreate, Long employeeId) {
        saveChatRoom(chatRoomCreate, employeeId);
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
        eventPublisher.publishEvent(chatRoomCreate);
    }

    /**
     * 채팅방 저장
     * @param chatRoomCreate 채팅방 생성 정보
     * @param employeeId 초대한 직원 아이디
     */
    private void saveChatRoom(ChatRoomCreate chatRoomCreate, Long employeeId) {
        int result = chatRoomMapper.saveChatRoom(chatRoomCreate, employeeId);
        if (result == 0) {
            throw new RuntimeException("채팅방 생성에 실패하였습니다.");
        }
    }

    /**
     * 채팅방 참여자 저장
     * @param chatRoomCreate 채팅방 생성 정보
     * @param employeeId 초대한 직원 아이디
     */
    private void saveParticipantEmployee(ChatRoomCreate chatRoomCreate, Long employeeId) {
        List<Long> employees = chatRoomCreate.getEmployees();
        for (Long participantEmployeeId : employees) {
            participationEmployeeMapper.saveParticipationEmployee(chatRoomCreate.getChatRoomId(), participantEmployeeId, employeeId);
        }
        participationEmployeeMapper.saveParticipationEmployee(chatRoomCreate.getChatRoomId(), employeeId, employeeId);
    }

    /**
     * 채팅방 목록 조회
     * @param employeeId 로그인한 직원 아이디
     * @param targetId 채팅방으로 이동할 직원 아이디 (대화하기 버튼 클릭시에만 존재)
     *                 채팅방으로 이동할 직원 아이디가 존재하지 않으면 null
     * 만약 employeeId와 targetId 둘만의 채팅방이 있다면 해당 채팅방 번호 return
     *                                 채팅방이 없다면 채팅방 생성 후 채팅방 번호 return
     *
     * 채팅방 이름
     * 채팅방 이름이 null일 경우 1:1 대화의 경우 상대방 이름으로 설정
     *                        그룹 대화일 경우 참여자 이름으로 설정
     *
     * 프로필
     * 1:1 대화일 경우 상대방 프로필 이미지로 설정
     * 그룹 대화일 경우 기본 이미지로 설정
     *
     * @return 채팅방 목록과 채팅방으로 이동할 채팅방 번호
     */
    @Transactional
    public ChatInfoAndRedirectNum getChatRoomList(Long employeeId, Long targetId) {
        List<ChatRoomInfo> chatRoomInfoList = chatRoomMapper.getChatRoomListByEmployeeId(employeeId);
        Long redirectNum = null;
        if (targetId != null) {
            Long chatRoomIdByEmployeeIds = chatRoomMapper.getChatRoomIdByEmployeeIds(employeeId, targetId);
            log.info("chatRoomIdByEmployeeIds : {}", chatRoomIdByEmployeeIds);
            if (chatRoomIdByEmployeeIds == null) {
                ChatRoomCreate chatRoomCreate = ChatRoomCreate.builder()
                        .chatRoomName("")
                        .employees(List.of(targetId)).build();
                createChatRoom(chatRoomCreate, employeeId);
                redirectNum = chatRoomCreate.getChatRoomId();
            } else {
                redirectNum = chatRoomIdByEmployeeIds;
            }
        }

        chatRoomInfoList.forEach(c -> {
            setChatRoomNameAndProfile(employeeId, c);
            setLastMessage(c);
        });

        return new ChatInfoAndRedirectNum(chatRoomInfoList, redirectNum);
    }

    /**
     * 채팅방 이름과 프로필 이미지 설정
     * 채팅방 참여자가 2명 이하일 경우 상대방의 프로필 이미지와 이름을 채팅방 정보에 설정 - 1:1 대화 일 경우
     * 채팅방 참여자가 3명 이상일 경우 채팅방 이름을 참여자 이름으로 설정 - 그룹 대화 일 경우
     * @param employeeId 로그인한 직원 아이디
     * @param chatRoomInfoBase 채팅방 정보
     */
    private void setChatRoomNameAndProfile(Long employeeId, ChatRoomInfoBase chatRoomInfoBase) {
        List<EmployeeInfo> employeeInfos = participationEmployeeMapper.getEmployeeByChatRoomIdExceptOneSelf(chatRoomInfoBase.getChatRoomId(), employeeId);

        if (chatRoomInfoBase.getParticipantCount() <= 2) {
            EmployeeInfo employeeInfo = employeeInfos.get(0);
            chatRoomInfoBase.setChatRoomProfileImageUrl(employeeInfo.getProfileUrl());
            chatRoomInfoBase.setEmployeeStatus(employeeInfo.getStatus());
            if (chatRoomInfoBase.getChatRoomName() == null) {
                chatRoomInfoBase.setChatRoomName(employeeInfo.getName());
            }
        } else {
            chatRoomInfoBase.setChatRoomProfileImageUrl(DEFAULT_GROUP_IMAGE);
            if (chatRoomInfoBase.getChatRoomName() == null) {
                List<String> employeeNames = employeeInfos.stream().map(EmployeeInfo::getName).toList();
                String chatRoomName = String.join(", ", employeeNames);
                chatRoomInfoBase.setChatRoomName(chatRoomName);
            }
        }

    }

    /**
     * 채팅방 상세 정보 조회
     *
     * @param chatRoomId 조회할 채팅방 아이디
     * @param employeeId 로그인한 직원 아이디
     * @param limit 채팅 메시지 조회 개수 - 50개씩 조회
     * @param offset 채팅 메시지 조회 시작 위치 - 0부터 시작
     * @return 채팅방 상세 정보
     */
    public ChatRoomDetailInfo getChatRoomDetailInfo(Long chatRoomId, Long employeeId, int limit, int offset) {
        ChatRoomDetailInfo chatRoomDetailInfo = chatRoomMapper.getChatRoomInfo(chatRoomId, employeeId)
                .orElseThrow(() -> new NotFoundException("채팅방이 존재하지 않습니다."));
        List<EmployeeInfo> employeeInfos = participationEmployeeMapper.getEmployeeByChatRoomIdExceptOneSelf(chatRoomId, employeeId);

        List<ChatInfo> chatInfos = chatRoomMapper.getChatInfoByPage(chatRoomId, employeeId, chatRoomDetailInfo.getParticipantCount(), limit, offset);

        chatInfos.forEach(c -> {
            if (c.getIsDeleted()) {
                c.setChatContents(DEFAULT_DELETE_MESSAGE);
                c.setChatType(ChatType.CHAT.getValue());
            }
        });

        chatRoomDetailInfo.setChatInfoList(chatInfos);

        setChatRoomNameAndProfile(employeeId, chatRoomDetailInfo);

        return chatRoomDetailInfo;
    }

    /**
     * 채팅 메시지 저장
     *
     * 입장중인 참여자를 고려하여 읽지 않은 메시지 개수 조회 및 채팅 상태 저장
     *
     * 알림
     * 채팅방에 들어와 있지 않고, FCM 토큰이 있는 참여자에게 FCM 메시지 전송
     *
     * 소켓
     * 채팅 메시지 저장 시 채팅방에 들어와 있는 참여자에게 채팅 메시지 이벤트 전송
     * 다른 소켓에 있더라도 채팅 목록을 조회할 수 있는 참여자에게 채팅 목록 이벤트 전송
     *
     * @param chatMessage 채팅 메시지 정보
     * @return 채팅 메시지 정보와 채팅 참여자 정보
     */
    @Transactional
    public ChatMessageInfo saveChatMessage(ChatMessage chatMessage) {
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
        eventPublisher.publishEvent(new PtptEmpInfos(participantEmployeeInfos));
        eventPublisher.publishEvent(chatMessageInfo);
        return chatMessageInfo;
    }

    /**
     * 읽지 않은 메시지 개수 조회 및 채팅 상태 저장
     * @param chatMessage 채팅 메시지 정보
     * @param participantEmployeeInfos 채팅 참여자 정보
     * @return
     */
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

    /**
     * 채팅 메시지 전송
     *
     * @param chatRoomId 채팅방 아이디
     * @param chatRoomName 채팅방 이름
     * @param chatProfileImageUrl 채팅방 프로필 이미지
     * @param message 채팅 메시지
     */
    public void sendChatMessageFcm(Long chatRoomId, String chatRoomName, String chatProfileImageUrl, String message) {
        List<EmployeeInfo> employeeInfos = participationEmployeeMapper.getFcmTokenByChatRoomId(chatRoomId);
        employeeInfos.forEach(e -> {
            fcmService.sendPush(SendPushDto.of(chatRoomName, message, chatProfileImageUrl), e.getEmployeeId());
        });

    }

    /**
     * 채팅방 이름 설정
     * @param chatRoomId 채팅방 아이디
     * @param employeeId 로그인한 직원 아이디
     * @return 채팅방 이름
     */
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

    /**
     * 채팅방 참여자 조회
     * @param chatRoomId 채팅방 아이디
     * @return 채팅방 참여자 정보
     */
    public List<EmployeeInfo> getChatRoomEmployees(Long chatRoomId) {
        return participationEmployeeMapper.getEmployeeInfoByChatRoomId(chatRoomId);
    }


    /**
     * 채팅방 삭제
     *
     * 채팅방 참여자 삭제, 채팅 메시지 저장, 채팅방 마지막 메시지 저장
     *
     * 소켓
     * 채팅방 삭제 시 채팅방에 참여한 참여자에게 채팅방 삭제 이벤트 전송
     * 다른 소켓에 있더라도 채팅 목록을 조회할 수 있는 참여자에게 채팅 목록 이벤트 전송
     *
     * @param chatRoomId 채팅방 아이디
     * @param employeeId 로그인한 직원 아이디
     * @return 채팅방 참여자 정보와 채팅 메시지 정보
     */
    @Transactional
    public void deleteChatRoom(Long chatRoomId, Long employeeId) {
        participationEmployeeMapper.deleteParticipationEmployee(chatRoomId, employeeId);

        EmployeeInfo employeeInfo = employeeService.getEmployeeInfo(employeeId);
        String message = employeeInfo.getName() + DEFAULT_DELETE_CHAT_ROOM_MESSAGE;

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

        eventPublisher.publishEvent(new PtptEmpInfos(participantEmployeeInfos));
        eventPublisher.publishEvent(chatMessageInfo);
    }

    /**
     * 채팅 삭제
     *
     * 소켓
     * 채팅이 삭제되면 채팅방에 참여한 참여자에게 채팅 삭제 이벤트 전송
     *
     * @param chatId 채팅 아이디
     * @param employeeId 로그인한 직원 아이디
     */
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

    /**
     * 채팅방 즐겨찾기 설정/삭제
     * @param chatRoomId 즐겨찾기 설정/삭제할 채팅방 아이디
     * @param employeeId 로그인한 직원 아이디
     */
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

    /**
     * 즐겨찾기 채팅방 조회
     * @param employeeId 로그인한 직원 아이디
     * @return 즐겨찾기 채팅방 목록
     */
    public List<ChatRoomInfo> getFavoriteChatRoom(Long employeeId) {
        List<ChatRoomInfo> favoriteChatRooms = chatRoomMapper.getFavoriteChatRooms(employeeId);

        favoriteChatRooms.forEach(c -> {
            setChatRoomNameAndProfile(employeeId, c);
            setLastMessage(c);
        });
        return favoriteChatRooms;
    }

    /**
     * 마지막 메시지 설정
     * @param chatRoomInfo 채팅방 정보
     */
    private static void setLastMessage(ChatRoomInfo chatRoomInfo) {
        if (chatRoomInfo.getLastMessage() == null) {
            chatRoomInfo.setLastMessage(DEFAULT_CREATE_CHAT_ROOM_MESSAGE);
        }
    }

}
