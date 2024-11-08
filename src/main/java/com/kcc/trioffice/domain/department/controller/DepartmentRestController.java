package com.kcc.trioffice.domain.department.controller;

import com.kcc.trioffice.domain.department.dto.response.Department;
import com.kcc.trioffice.domain.department.dto.response.DepartmentUtils;
import com.kcc.trioffice.domain.department.dto.response.TreeNode;
import com.kcc.trioffice.domain.department.service.DepartmentService;
import com.kcc.trioffice.global.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/api/departments/{deptId}")
    public ResponseEntity<Department> getDepartment(@PathVariable Long deptId) {
        Department department = departmentService.getDepartmentById(deptId); // 부서 정보 가져오기
        log.info("department = {}", department);
        return ResponseEntity.ok(department); // 부서 수정 폼 페이지 경로
    }

    @GetMapping("/api/departments/{deptId}/sub") // 하위 부서를 가져오는 메서드
    public ResponseEntity<List<Department>> getSubDepartments(@PathVariable Long deptId) {
        List<Department> subDepartments = departmentService.getSubDepartments(deptId);
        return ResponseEntity.ok(subDepartments);
    }

    // 부서 저장 처리
//    @PostMapping("/api/departments/save")
//    @ResponseBody
//    public ResponseEntity<String> saveDepartment(
//            @RequestParam(required = false) Long upperDeptId, // 선택 사항으로 수정
//            @RequestParam String departmentName) {
//        System.out.println("upperDeptId = " + upperDeptId);
//
//        Department department = new Department();
//
//        department.setDepartmentName(departmentName);
//
//        // upperDeptId가 null인 경우에 대한 처리 (예: 최상위 부서로 설정)
//        if (upperDeptId == 0) {
//            System.out.println(upperDeptId + "@@@@@@@@@@@@@");
//            // null일 경우의 동작 (예: 최상위 부서로 설정)
//            departmentService.insertTopDepartment(department);
//        } else {
//            department.setUpperDeptId(upperDeptId);
//            departmentService.saveDepartment(department);  // 부서 저장
//        }
//
//        return ResponseEntity.ok("부서가 성공적으로 추가되었습니다.");
//    }


    @PostMapping("/api/departments/save")
    @ResponseBody
    public ResponseEntity<String> saveDepartment(
            @RequestParam(required = false) Long upperDeptId,
            @RequestParam String departmentName) {
        Department department = new Department();
        department.setDepartmentName(departmentName);

        if (upperDeptId == null || upperDeptId == 0) {
            departmentService.insertTopDepartment(department);
        } else {
            department.setUpperDeptId(upperDeptId);
            departmentService.saveDepartment(department);
        }

        // 유틸리티 메서드를 사용하여 추가된 부서의 HTML 생성
        String departmentHTML = DepartmentUtils.renderDept(department);

        return ResponseEntity.ok(departmentHTML); // HTML 반환
    }


    // 부서 삭제 처리
    @DeleteMapping("/api/delete/{deptId}")
    public void deleteDepartment(@PathVariable Long deptId) {
        System.out.println("Deleting department with id: " + deptId);
        departmentService.deleteDepartment(deptId);
    }


    // 부서 수정 메서드
    @PutMapping("/api/update/{deptId}")
    public void updateDepartment(@PathVariable Long deptId, @RequestBody Department updatedDepartment) {
        System.out.println("deptId" + deptId);
        System.out.println("updatedDepartment" + updatedDepartment);

        System.out.println("하이영두");
        updatedDepartment.setDeptId(deptId); // 기존 deptId 설정 (클라이언트에서 받은 deptId와 일치)
        System.out.println("하이영두");
        departmentService.updateDepartment(updatedDepartment);
    }


}
