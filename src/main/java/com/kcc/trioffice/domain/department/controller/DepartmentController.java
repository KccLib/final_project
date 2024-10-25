package com.kcc.trioffice.domain.department.controller;

import com.kcc.trioffice.domain.department.dto.response.Department;
import com.kcc.trioffice.domain.department.service.DepartmentService;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    // 모든 부서 조회
    @GetMapping
    public String getDepartment(Model model) {
        List<Department> departments = departmentService.getDepartmentDetails();
        model.addAttribute("departmentTree", departments);
        log.info("departments : {}", departments);
        return "department/department"; // JSP 또는 Thymeleaf 템플릿 이름
    }

    // 특정 부서의 직원 정보 조회
    @GetMapping("/{deptId}/employees")
    public String getEmployeesByDepartment(@PathVariable Long deptId, Model model) {
        List<EmployeeInfo> employees = departmentService.getEmployeesByDeptId(deptId);
        model.addAttribute("employees", employees);
        model.addAttribute("deptId", deptId);
        log.info("employees in department {}: {}", deptId, employees);
        return "department/employees"; // 직원 목록을 표시할 JSP 또는 Thymeleaf 템플릿 이름
    }

    @GetMapping("/admin")
    public String getAdminDepartment(Model model) {
        List<Department> departments = departmentService.getDepartmentDetails();
        model.addAttribute("departmentTree", departments);
        return "department/adminDepartment";
    }

    // 부서 추가 폼 페이지
    @GetMapping("/department/add")
    public String showAddDepartmentForm(Model model) {
        List<Department> parentDepartments = departmentService.getDepartmentDetails(); // 상위 부서 목록
        model.addAttribute("parentDepartments", parentDepartments);
        return "department/addDepartment"; // 부서 추가 폼 페이지 경로
    }

    // 부서 저장 처리
    @PostMapping("/saveDepartment")
    public String saveDepartment(@RequestParam Long upperDeptId, @RequestParam String departmentName, Model model) {
        Department department = new Department();
        department.setUpperDeptId(upperDeptId);
        department.setDepartmentName(departmentName);

        departmentService.saveDepartment(department);  // 부서 저장

        // 부서 저장 후 다시 부서 목록을 불러오기
        List<Department> departments = departmentService.getDepartmentDetails();
        model.addAttribute("departmentTree", departments);
        return "redirect:/departments/admin"; // 부서 목록을 보여주는 페이지로 리다이렉트
    }

    // 부서 수정 폼 페이지
    @GetMapping("/department/edit/{deptId}")
    public String showEditDepartmentForm(@PathVariable Long deptId, Model model) {
        Department department = departmentService.getDepartmentById(deptId); // 부서 정보 가져오기
        List<Department> parentDepartments = departmentService.getDepartmentDetails(); // 상위 부서 목록
        model.addAttribute("department", department);
        model.addAttribute("parentDepartments", parentDepartments);
        return "department/editDepartment"; // 부서 수정 폼 페이지 경로
    }

    // 부서 수정 처리
    @PostMapping("/updateDepartment/{deptId}")
    public String updateDepartment(@PathVariable Long deptId, @RequestParam Long upperDeptId, @RequestParam String departmentName) {
        Department department = new Department();
        department.setDeptId(deptId);
        department.setUpperDeptId(upperDeptId);
        department.setDepartmentName(departmentName);

        departmentService.updateDepartment(department); // 부서 수정

        return "redirect:/departments/admin"; // 부서 목록으로 리다이렉트
    }

    // 부서 삭제 처리 (소프트 삭제)
    @PatchMapping("/deleteDepartment/{deptId}")
    public String deleteDepartment(@PathVariable Long deptId) {
        departmentService.deleteDepartment(deptId); // 부서 삭제 (is_deleted 값 변경)
        return "redirect:/departments/admin"; // 부서 목록으로 리다이렉트
    }


}
