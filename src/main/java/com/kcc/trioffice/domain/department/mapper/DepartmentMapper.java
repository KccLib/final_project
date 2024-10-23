package com.kcc.trioffice.domain.department.mapper;

import com.kcc.trioffice.domain.department.dto.response.Department;
import com.kcc.trioffice.domain.department.dto.response.DepartmentInfo;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    // 특정 부서 ID에 대한 직원 정보 가져오기
    List<EmployeeInfo> getEmployeesByDeptId(@Param("deptId") Long deptId);

    // 추가적인 부서 정보 조회 메서드 (예: 모든 부서와 하위 부서)
    List<Department> getDepartmentDetails();

    List<DepartmentInfo> getDepartments(Long companyId);
}
