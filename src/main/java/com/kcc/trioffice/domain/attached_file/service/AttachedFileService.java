package com.kcc.trioffice.domain.attached_file.service;

import com.kcc.trioffice.domain.attached_file.dto.request.AttachedFileSelect;
import com.kcc.trioffice.domain.attached_file.dto.response.AttachedFileDetailInfo;
import com.kcc.trioffice.domain.attached_file.dto.response.AttachedFileInfo;
import com.kcc.trioffice.domain.attached_file.dto.response.ImageInfo;
import com.kcc.trioffice.domain.attached_file.mapper.AttachedFileMapper;
import com.kcc.trioffice.domain.attached_file.mapper.TagMapper;
import com.kcc.trioffice.domain.chat_room.dto.request.ChatMessage;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatMessageInfo;
import com.kcc.trioffice.domain.chat_room.dto.response.ChatRoomInfoBase;
import com.kcc.trioffice.domain.chat_room.dto.response.ParticipantEmployeeInfo;
import com.kcc.trioffice.domain.chat_room.mapper.ChatMapper;
import com.kcc.trioffice.domain.chat_room.mapper.ChatRoomMapper;
import com.kcc.trioffice.domain.chat_room.service.ChatRoomService;
import com.kcc.trioffice.domain.chat_status.mapper.ChatStatusMapper;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.service.EmployeeService;
import com.kcc.trioffice.domain.participation_employee.dto.response.PtptEmpInfos;
import com.kcc.trioffice.domain.participation_employee.mapper.ParticipationEmployeeMapper;
import com.kcc.trioffice.global.enums.ChatType;
import com.kcc.trioffice.global.enums.FileType;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import com.kcc.trioffice.global.image.FilePathUtils;
import com.kcc.trioffice.global.image.S3SaveDir;
import com.kcc.trioffice.global.image.dto.response.S3UploadFile;
import com.kcc.trioffice.global.image.service.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kcc.trioffice.global.constant.GlobalConstants.DEFAULT_GROUP_IMAGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttachedFileService {

    private final S3FileService s3FileService;
    private final ChatMapper chatMapper;
    private final AttachedFileMapper attachedFileMapper;
    private final TagMapper tagMapper;
    private final ChatRoomMapper chatRoomMapper;
    private final EmployeeService employeeService;
    private final ParticipationEmployeeMapper participationEmployeeMapper;
    private final ChatStatusMapper chatStatusMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방에 파일 전송
     *
     * 여러개의 파일을 받을 수 있고, 파일 하나당 여러개의 태그, 하나의 채팅을 가짐
     *
     * 알림
     * - 채팅방에 파일을 전송하면 채팅방에 참여중인 사람들에게 파일을 전송
     * - 채팅 목록 최신화하라는 알림 전송
     *
     * @param multipartFiles
     * @param tags
     * @param chatRoomId
     * @param employeeId
     */
    @Transactional
    public void sendAttachedFile(List<MultipartFile> multipartFiles, List<String> tags, Long chatRoomId, Long employeeId) {
        EmployeeInfo employeeInfo = employeeService.getEmployeeInfo(employeeId);

        List<ChatMessageInfo> chatMessageInfos = new ArrayList<>();
        List<ParticipantEmployeeInfo> participantEmployeeInfos = participationEmployeeMapper.getParticipantEmployeeInfoByChatRoomId(chatRoomId);


        for (MultipartFile multipartFile : multipartFiles) {
            log.info("multipartFile: {}", multipartFile);

            String s3UploadFilePath = FilePathUtils.createS3UploadFilePath(multipartFile);
            S3UploadFile s3UploadFile = s3FileService.upload(multipartFile, s3UploadFilePath, S3SaveDir.CHAT);

            ChatMessage chatMessage = ChatMessage.builder()
                    .roomId(chatRoomId)
                    .senderId(employeeId)
                    .message(s3UploadFile.getFileName())
                    .chatType(ChatType.convertFileType(s3UploadFile.getFileType()))
                    .build();
            chatMapper.saveChatMessage(chatMessage);

            attachedFileMapper.saveAttachedFile(chatMessage.getChatId(), employeeId, s3UploadFile);
            saveTag(tags, employeeId, s3UploadFile);
            chatRoomMapper.updateChatRoomLastMessage(chatMessage.getRoomId(), chatMessage.getChatId());
            int unreadCount = 0;

            //채팅 상태 생성
            for (ParticipantEmployeeInfo participantEmployeeInfo : participantEmployeeInfos) {
                if (participantEmployeeInfo.getEmployeeId().equals(chatMessage.getSenderId()) || participantEmployeeInfo.getIsEntered()) {
                    chatStatusMapper.saveChatStatusRead(chatMessage.getRoomId(), chatMessage.getChatId(), participantEmployeeInfo.getEmployeeId(), chatMessage.getSenderId());
                } else {
                    unreadCount++;
                    chatStatusMapper.saveChatStatus(chatMessage.getRoomId(), chatMessage.getChatId(), participantEmployeeInfo.getEmployeeId(), chatMessage.getSenderId());
                }
            }

            ChatMessageInfo chatMessageInfo = getChatMessageInfo(tags, chatRoomId, employeeId, s3UploadFile, chatMessage, employeeInfo, unreadCount);
            chatMessageInfos.add(chatMessageInfo);
        }

        String chatRoomName = chatRoomService.handleChatRoomName(chatRoomId, employeeId);
        chatRoomService.sendChatMessageFcm(chatRoomId, chatRoomName, employeeInfo.getProfileUrl(), "파일을 전송했습니다.");
        eventPublisher.publishEvent(new PtptEmpInfos(participantEmployeeInfos));
        chatMessageInfos.forEach(eventPublisher::publishEvent);
    }

    /**
     * 채팅 메세지 정보 생성
     *
     * 파일인지, 이미지인지에 따라 다르게 DTO 생성
     *
     * @param tags
     * @param chatRoomId
     * @param employeeId
     * @param s3UploadFile
     * @param chatMessage
     * @param employeeInfo
     * @param unreadCount
     * @return
     */
    private static ChatMessageInfo getChatMessageInfo(List<String> tags, Long chatRoomId, Long employeeId, S3UploadFile s3UploadFile, ChatMessage chatMessage, EmployeeInfo employeeInfo, int unreadCount) {
        ChatMessageInfo chatMessageInfo = null;


        if(s3UploadFile.getFileType() == 1) {
             chatMessageInfo = ChatMessageInfo.builder()
                    .chatTime(LocalDateTime.now())
                    .chatContents(s3UploadFile.getFileUrl())
                    .chatId(chatMessage.getChatId())
                    .senderName(employeeInfo.getName())
                    .senderProfileUrl(employeeInfo.getProfileUrl())
                    .senderId(employeeId)
                    .chatType(ChatType.IMAGE.toString())
                    .roomId(chatRoomId)
                    .tags(tags)
                    .unreadMessageCount(unreadCount)
                    .build();
        } else {
            chatMessageInfo = ChatMessageInfo.builder()
                    .chatTime(LocalDateTime.now())
                    .chatContents(s3UploadFile.getFileName())
                    .chatId(chatMessage.getChatId())
                    .senderName(s3UploadFile.getFileName())
                    .senderProfileUrl(employeeInfo.getProfileUrl())
                    .senderId(employeeId)
                    .chatType(ChatType.FILE.toString())
                    .roomId(chatRoomId)
                    .tags(tags)
                    .unreadMessageCount(unreadCount)
                    .build();
        }
        return chatMessageInfo;
    }

    /**
     * 태그 저장
     *
     * @param tags 저장할 태그 목록
     * @param employeeId 저장한 직원 아이디
     * @param s3UploadFile 저장한 파일 정보
     */
    private void saveTag(List<String> tags, Long employeeId, S3UploadFile s3UploadFile) {
        if (tags != null) {
            tags.forEach(
                    tag -> tagMapper.saveTag(s3UploadFile.getFileId(), tag, employeeId)
            );
        }
    }

    /**
     * 채팅방에 파일 다운로드
     *
     * @param chatId 다운로드할 파일이 있는 채팅 번호
     * @param employeeId 로그인한 사용자 정보
     * @return 다운로드할 파일
     */
    public ResponseEntity<byte[]> downloadAttachedFile(Long chatId, Long employeeId) {
        S3UploadFile s3UploadFile = attachedFileMapper.getAttachedFileByChatId(chatId).orElseThrow(
                () -> new NotFoundException("해당 파일이 존재하지 않습니다."));
        return s3FileService.download(s3UploadFile.getFileUrl(), s3UploadFile.getFileName());
    }

    /**
     * 채팅방에 파일 정보 조회
     *
     * @param chatRoomId
     * @param employeeId
     * @param limit
     * @param offset
     * @param searchType
     * @param tags
     * @return
     */
    public List<AttachedFileInfo> getAttachedFile(Long chatRoomId, Long employeeId, int limit, int offset, String searchType, List<String> tags) {
        return attachedFileMapper.getAttachedFile(chatRoomId, limit, offset, searchType, tags);
    }

    /**
     * 채팅방에 이미지 조회
     *
     * @param chatRoomId
     * @param employeeId
     * @param searchType
     * @param tags
     * @return
     */
    public List<ImageInfo> getImage(Long chatRoomId, Long employeeId, String searchType, List<String> tags) {
        return attachedFileMapper.getImages(chatRoomId, searchType, tags);
    }

    /**
     * 이미지 다운로드
     *
     * @param fileId
     * @param employeeId
     * @return
     */
    public ResponseEntity<byte[]> downloadImage(Long fileId, Long employeeId) {
        S3UploadFile s3UploadFile = attachedFileMapper.getAttachedFileByFileId(fileId).orElseThrow(
                () -> new NotFoundException("해당 파일이 존재하지 않습니다."));
        return s3FileService.download(s3UploadFile.getFileUrl(), s3UploadFile.getFileName());
    }

    public List<AttachedFileDetailInfo> getAllAttachedFile(Long currentEmployeeId, AttachedFileSelect attachedFileSelect) {
        List<AttachedFileDetailInfo> attachedFileDetailInfos = attachedFileMapper.getAllAttachedFile(currentEmployeeId,
                attachedFileSelect.getExtensions(),
                attachedFileSelect.getSenderId(),
                attachedFileSelect.getStartDate(),
                attachedFileSelect.getEndDate(),
                attachedFileSelect.getKeyword(),
                attachedFileSelect.getOffset(),
                50L);

        attachedFileDetailInfos.stream().filter(a -> a.getChatRoomName() == null)
                .forEach(a -> setChatRoomNameAndProfile(currentEmployeeId, a));

        log.info("attachedFileDetailInfos: {}", attachedFileDetailInfos);

        return attachedFileDetailInfos;
    }

    private void setChatRoomNameAndProfile(Long employeeId, AttachedFileDetailInfo attachedFileDetailInfo) {
        List<EmployeeInfo> employeeInfos = participationEmployeeMapper.getEmployeeByChatRoomIdExceptOneSelf(attachedFileDetailInfo.getChatRoomId(), employeeId);

        if (employeeInfos.size() == 1) {
            EmployeeInfo employeeInfo = employeeInfos.get(0);
            attachedFileDetailInfo.setChatRoomName(employeeInfo.getName());
        } else {
            List<String> employeeNames = employeeInfos.stream().map(EmployeeInfo::getName).toList();
            String chatRoomName = String.join(", ", employeeNames);
            attachedFileDetailInfo.setChatRoomName(chatRoomName);
        }

    }

    public ResponseEntity<?> previewAttachedFile(Long fileId) {
        S3UploadFile s3UploadFile = attachedFileMapper.getAttachedFileByFileId(fileId)
                .orElseThrow(() -> new NotFoundException("해당 파일이 존재하지 않습니다."));

        // Determine the file type
        String fileExtension = s3UploadFile.getFileExtension().toLowerCase();
        FileType fileType = s3UploadFile.getFileType() == 1 ? FileType.IMAGE : FileType.FILE;

        // For images, return the image directly
        if (fileType == FileType.IMAGE) {
            return s3FileService.getFileAsResponseEntity(s3UploadFile.getFileUrl(), s3UploadFile.getFileName(), MediaType.IMAGE_JPEG);
        }

        // For PDFs, return the PDF content
        if (fileExtension.equals("pdf")) {
            return s3FileService.getFileAsResponseEntity(s3UploadFile.getFileUrl(), s3UploadFile.getFileName(), MediaType.APPLICATION_PDF);
        }

        // For other file types, return an appropriate response
        // e.g., a message indicating that preview is not available
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body("Preview not available for this file type.");
    }

}
