package com.kcc.trioffice.domain.attached_file.controller;

import com.kcc.trioffice.domain.attached_file.dto.request.AttachedFileSelect;
import com.kcc.trioffice.domain.attached_file.dto.response.AttachedFileDetailInfo;
import com.kcc.trioffice.domain.attached_file.service.AttachedFileService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AttachedFileController {

    private final AttachedFileService attachedFileService;

    @GetMapping("/attached-files")
    public String attachedFile(@AuthenticationPrincipal PrincipalDetail principalDetail,
                               @ModelAttribute AttachedFileSelect attachedFileSelect,
                               Model model) {
        List<AttachedFileDetailInfo> attachedFileDetailInfos = attachedFileService.getAllAttachedFile(principalDetail.getEmployeeId(), attachedFileSelect);
        model.addAttribute("attachedFileDetailInfos", attachedFileDetailInfos);
        return "attached-file/attached-file";
    }
}
