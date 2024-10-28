package com.kcc.trioffice.domain.chat_status.service;

import com.kcc.trioffice.domain.chat_room.dto.response.ChatDetailInfo;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatRoomEnter;
import com.kcc.trioffice.domain.chat_room.mapper.ChatMapper;
import com.kcc.trioffice.domain.chat_status.dto.response.ChatStatusInfo;
import com.kcc.trioffice.domain.chat_status.dto.response.EmoticonMessage;
import com.kcc.trioffice.domain.chat_status.dto.response.EmoticonStatus;
import com.kcc.trioffice.domain.chat_status.mapper.ChatStatusMapper;
import com.kcc.trioffice.domain.participation_employee.mapper.ParticipationEmployeeMapper;
import com.kcc.trioffice.global.enums.EmoticonType;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatStatusService {

    private final ChatStatusMapper chatStatusMapper;
    private final ChatMapper chatMapper;
    private final ParticipationEmployeeMapper participationEmployeeMapper;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * 채팅방에 이모티콘을 업데이트
     * 이모티콘을 업데이트하고 이모티콘 메세지를 반환
     *
     * 소켓
     * 채팅방에 참여중인 사람들에게 이모티콘 메세지를 전송
     *
     * @param chatId
     * @param emoticonType
     * @param employeeId
     * @return
     */
    @Transactional
    public void updateEmoticon(Long chatId, Long chatRoomId, String emoticonType, Long employeeId) {

        ChatDetailInfo chatDetailInfo = chatMapper.getChatDetailInfo(chatId).orElseThrow(
                () -> new NotFoundException("채팅 정보가 존재하지 않습니다."));

        ChatStatusInfo chatStatusInfo = chatStatusMapper.getChatStatusByChatIdAndEmployeeId(chatId, employeeId).orElseThrow(
                () -> new NotFoundException("채팅 상태 정보가 존재하지 않습니다."));


        long emoticonTypeNumber = EmoticonType.toValue(emoticonType);

        boolean isEmoticon = getIsEmoticon(chatStatusInfo, emoticonTypeNumber);

        chatStatusMapper.updateEmoticon(chatId, employeeId, emoticonTypeNumber, isEmoticon);
        EmoticonMessage emoticonMessage = new EmoticonMessage("EMOTICON", chatRoomId, chatId, chatDetailInfo.getSenderId(), employeeId, emoticonType, true);

        eventPublisher.publishEvent(emoticonMessage);
    }

    /**
     * 채팅방의 바꿔야 할 이모티콘 상태를 반환
     * @param chatStatusInfo 채팅 상태 정보
     * @param emoticonTypeNumber 이모티콘 타입
     * @return
     */
    private boolean getIsEmoticon(ChatStatusInfo chatStatusInfo, long emoticonTypeNumber) {
        if (chatStatusInfo.getEmoticonType() == null || chatStatusInfo.getEmoticonType() != emoticonTypeNumber ||
                (chatStatusInfo.getEmoticonType() == emoticonTypeNumber && !chatStatusInfo.getIsEmoticon())) {
            return true;
        }
        return false;
    }

    /**
     * 채팅방에 입장
     * 채팅방에 입장하고 읽지 않은 메세지를 읽음 처리
     *
     * 소켓
     * 채팅방에 참여중인 사람들에게 읽음 처리 메세지를 전송
     *
     * @param chatRoomId 채팅방 아이디
     * @param employeeId 로그인한 직원 아이디
     * @return ChatRoomEnter 입장한 채팅방 정보 (채팅방 ID, 메세지 상태, 입장한 사람 ID, 읽지 않았던 메세지 ID)
     */
    @Transactional
    public ChatRoomEnter enterChatRoom(Long chatRoomId, Long employeeId) {
        List<Long> unreadMessageId = chatStatusMapper.getUnreadMessageId(chatRoomId, employeeId);
        chatStatusMapper.updateChatStatusRead(chatRoomId, employeeId);

        participationEmployeeMapper.updateIsEntered(chatRoomId, employeeId, true);

        return new ChatRoomEnter(chatRoomId, "READ", employeeId, unreadMessageId);
    }

    /**
     * 채팅방의 이모티콘 상태 정보를 반환
     * @param chatId 채팅 아이디
     * @param employeeId 로그인한 직원 아이디
     * @return EmoticonStatus 이모티콘 상태 정보
     */
    public EmoticonStatus getEmoticonCount(Long chatId, Long employeeId) {
        return chatStatusMapper.getEmoticonCount(chatId, employeeId).orElseThrow(
                () -> new NotFoundException("채팅 상태 정보가 존재하지 않습니다."));
    }

    /**
     * 채팅방 연결 해제
     *
     * @param employeeId
     */
    @Transactional
    public void disconnectChatRoom(Long employeeId) {
        log.info("disconnectChatRoom employeeId : {}", employeeId);
        participationEmployeeMapper.disconnectAllChatRoom(employeeId);
    }
}
