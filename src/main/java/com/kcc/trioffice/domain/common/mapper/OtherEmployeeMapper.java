package com.kcc.trioffice.domain.common.mapper;


import com.kcc.trioffice.domain.common.domain.EmployeeInfoWithDept;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface OtherEmployeeMapper {
    Optional<EmployeeInfoWithDept> getEmployeeInfoWithDept(Long id);
}
