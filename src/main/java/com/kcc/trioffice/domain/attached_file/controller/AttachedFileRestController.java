package com.kcc.trioffice.domain.attached_file.controller;

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

    @PostMapping("/api/chatrooms/{chatRoomId}/attached-file/send")
    public void sendAttachedFile(@RequestParam(value = "tags", required = false) List<String> tags,
                                 @PathVariable Long chatRoomId,
                                 @AuthenticationPrincipal PrincipalDetail principalDetail,
                                 HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFiles = new ArrayList<>(multipartRequest.getFileMap().values());

        attachedFileService.sendAttachedFile(multipartFiles, tags, chatRoomId, principalDetail.getEmployeeId());
    }

    @GetMapping("/api/chatrooms/chats/{chatId}/attached-file/download")
    public ResponseEntity<byte[]> downloadAttachedFile(@PathVariable Long chatId,
                                                       @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return attachedFileService.downloadAttachedFile(chatId, principalDetail.getEmployeeId());
    }

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

    @GetMapping("/api/chatrooms/{chatRoomId}/image")
    public ResponseEntity<List<ImageInfo>> getImage(@PathVariable Long chatRoomId,
                                                    @RequestParam(required = false) String searchType,
                                                    @RequestParam(required = false) List<String> tags,
                                                    @AuthenticationPrincipal PrincipalDetail principalDetail) {
        log.info("searchType : {}", searchType);
        List<ImageInfo> imageInfos = attachedFileService.getImage(chatRoomId, principalDetail.getEmployeeId(), searchType, tags);
        return ResponseEntity.ok(imageInfos);
    }

    @GetMapping("/api/chatrooms/image/{fileId}/download")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long fileId,
                                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return attachedFileService.downloadImage(fileId, principalDetail.getEmployeeId());
    }


}
