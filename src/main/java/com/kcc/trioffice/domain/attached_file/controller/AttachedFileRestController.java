package com.kcc.trioffice.domain.attached_file.controller;

import com.kcc.trioffice.domain.attached_file.dto.request.AttachedFileSelect;
import com.kcc.trioffice.domain.attached_file.dto.response.AttachedFileDetailInfo;
import com.kcc.trioffice.domain.attached_file.dto.response.AttachedFileInfo;
import com.kcc.trioffice.domain.attached_file.dto.response.ImageInfo;
import com.kcc.trioffice.domain.attached_file.service.AttachedFileService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AttachedFileRestController {

    private final AttachedFileService attachedFileService;

    /**
     * 채팅방에 파일 전송
     *
     * @param tags 파일에 대한 태그
     * @param chatRoomId 파일을 보낼 채팅방 번호
     * @param principalDetail 로그인한 사용자 정보
     * @param request 파일을 받기 위한 request 정보
     */
    @PostMapping("/api/chatrooms/{chatRoomId}/attached-file/send")
    public void sendAttachedFile(@RequestParam(value = "tags", required = false) List<String> tags,
                                 @PathVariable Long chatRoomId,
                                 @AuthenticationPrincipal PrincipalDetail principalDetail,
                                 HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFiles = new ArrayList<>(multipartRequest.getFileMap().values());

        attachedFileService.sendAttachedFile(multipartFiles, tags, chatRoomId, principalDetail.getEmployeeId());
    }

    /**
     * 채팅방에 파일 다운로드
     *
     * @param chatId 다운로드할 파일이 있는 채팅 번호
     * @param principalDetail 로그인한 사용자 정보
     * @return
     */
    @GetMapping("/api/chatrooms/chats/{chatId}/attached-file/download")
    public ResponseEntity<byte[]> downloadAttachedFile(@PathVariable Long chatId,
                                                       @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return attachedFileService.downloadAttachedFile(chatId, principalDetail.getEmployeeId());
    }

    /**
     * 채팅방에 파일 정보 조회
     *
     * @param chatRoomId 채팅방 번호
     * @param limit 가져올 파일 갯수
     * @param offset 가져올 파일 시작점
     * @param searchType 검색 타입
     * @param tags 검색할 태그
     * @param principalDetail 로그인한 사용자 정보
     * @return
     */
    @GetMapping("/api/chatrooms/{chatRoomId}/attached-files")
    public ResponseEntity<List<AttachedFileInfo>> getAttachedFile(@PathVariable Long chatRoomId,
                                             @RequestParam int limit,
                                             @RequestParam int offset,
                                             @RequestParam String searchType,
                                             @RequestParam(required = false) List<String> tags,
                                             @AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<AttachedFileInfo> attachedFile = attachedFileService.getAttachedFile(chatRoomId, principalDetail.getEmployeeId(), limit, offset, searchType, tags);
        return ResponseEntity.ok(attachedFile);
    }

    /**
     * 채팅방에 이미지 조회
     *
     * @param chatRoomId 채팅방 번호
     * @param searchType 검색 타입
     * @param tags 검색할 태그
     * @param principalDetail  로그인한 사용자 정보
     * @return
     */
    @GetMapping("/api/chatrooms/{chatRoomId}/image")
    public ResponseEntity<List<ImageInfo>> getImage(@PathVariable Long chatRoomId,
                                                    @RequestParam(required = false) String searchType,
                                                    @RequestParam(required = false) List<String> tags,
                                                    @AuthenticationPrincipal PrincipalDetail principalDetail) {
        log.info("searchType : {}", searchType);
        List<ImageInfo> imageInfos = attachedFileService.getImage(chatRoomId, principalDetail.getEmployeeId(), searchType, tags);
        return ResponseEntity.ok(imageInfos);
    }

    /**
     * 이미지 다운로드
     *
     * @param fileId 다운로드할 이미지 파일 번호
     * @param principalDetail 로그인한 사용자 정보
     * @return
     */
    @GetMapping("/api/chatrooms/image/{fileId}/download")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long fileId,
                                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return attachedFileService.downloadImage(fileId, principalDetail.getEmployeeId());
    }

    @GetMapping("/api/attached-files")
    public List<AttachedFileDetailInfo> attachedFile(@AuthenticationPrincipal PrincipalDetail principalDetail,
                               @ModelAttribute AttachedFileSelect attachedFileSelect) {
        log.info("attachedFileSelect : {}", attachedFileSelect);
        return attachedFileService.getAllAttachedFile(principalDetail.getEmployeeId(), attachedFileSelect);
    }

    @GetMapping("/api/attached-files/{fileId}/preview")
    public ResponseEntity<?> previewFile(@PathVariable Long fileId) {
        return attachedFileService.previewAttachedFile(fileId);
    }

}
