package com.kcc.trioffice.domain.common.service;


import com.kcc.trioffice.domain.common.domain.EmployeeInfoWithDept;
import com.kcc.trioffice.domain.common.mapper.OtherEmployeeMapper;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.global.constant.GlobalConstants;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtherEmployeeService {

    private final OtherEmployeeMapper otherEmployeeMapper;
    private final EmployeeMapper employeeMapper;

    public EmployeeInfoWithDept getOtherEmployeeInfo(Long id) {

        EmployeeInfoWithDept employeeInfo = otherEmployeeMapper.getEmployeeInfoWithDept(id)
                .orElseThrow(() -> new NotFoundException("회원정보를 찾을 수 없습니다."));

        String employeeDefaultProfile = GlobalConstants.DEFAULT_EMPLOYEE_PROFILE;
        if(employeeInfo.getProfileUrl() == null) {
            employeeInfo.setProfileUrl(employeeDefaultProfile);
        }

        return employeeInfo;
    }

}
