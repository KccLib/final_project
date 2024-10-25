package com.kcc.trioffice.domain.department.service;

import com.kcc.trioffice.domain.department.dto.response.Department;
import com.kcc.trioffice.domain.department.dto.response.DepartmentInfo;
import com.kcc.trioffice.domain.department.dto.response.TreeNode;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.department.mapper.DepartmentMapper;
import com.kcc.trioffice.domain.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final EmployeeService employeeService;

    public List<Department> getDepartmentDetails() {
        // 모든 최상위 부서 가져오기
        List<Department> departments = departmentMapper.getDepartmentDetails();

        // 각 부서에 대해 직원 정보를 추가
        for (Department department : departments) {
            List<EmployeeInfo> employees = departmentMapper.getEmployeesByDeptId(department.getDeptId());
            department.setEmployees(employees);
        }

        return departments;
    }

    // 특정 부서의 직원 정보를 가져오는 메서드
    public List<EmployeeInfo> getEmployeesByDeptId(Long deptId) {
        return departmentMapper.getEmployeesByDeptId(deptId);
    }

    public List<TreeNode> getDepartmentTree(Long employeeId) {
        List<TreeNode> treeNodes = new ArrayList<>();
        EmployeeInfo employeeInfo = employeeService.getEmployeeInfo(employeeId);
        List<DepartmentInfo> departments = departmentMapper.getDepartments(employeeInfo.getCompanyId());
        departments.stream().map(TreeNode::of).forEach(treeNodes::add);

        employeeService.getEmployeeByCompanyId(employeeId).stream().map(TreeNode::of).forEach(treeNodes::add);

        return treeNodes;
    }


    // 부서 추가 메서드
    public void saveDepartment(Department department) {
        departmentMapper.insertDepartment(department);
    }

    // 부서 수정 메서드 추가
    public void updateDepartment(Department department) {
        departmentMapper.updateDepartment(department);
    }

    // 부서 삭제 메서드 추가
    public void deleteDepartment(Long deptId) {
        departmentMapper.deleteDepartment(deptId);
    }

    // 특정 부서 정보 조회
    public Department getDepartmentById(Long deptId) {
        return departmentMapper.findDepartmentById(deptId); // 매퍼를 통해 특정 부서 정보 조회
    }

}
