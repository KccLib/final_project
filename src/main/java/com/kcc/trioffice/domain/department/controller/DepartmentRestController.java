package com.kcc.trioffice.domain.department.controller;

import com.kcc.trioffice.domain.department.dto.response.TreeNode;
import com.kcc.trioffice.domain.department.service.DepartmentService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DepartmentRestController {

    private final DepartmentService departmentService;

    @GetMapping("/api/departments/tree")
    public ResponseEntity<List<TreeNode>> getDepartmentTree(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<TreeNode> treeNodes = departmentService.getDepartmentTree(principalDetail.getEmployeeId());
        log.info("treeNodes: {}", treeNodes);
        return ResponseEntity.ok(treeNodes);
    }
}
