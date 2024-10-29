package com.kcc.trioffice.global.handler;

import com.kcc.trioffice.domain.employee.dto.request.SaveFcmToken;
import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.domain.employee.service.EmployeeService;
import com.kcc.trioffice.global.enums.StatusType;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final EmployeeMapper employeeMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfoFindByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));
        employeeMapper.saveFcmToken(employeeInfo.getEmployeeId(), "");
        employeeMapper.changeEmployeeStatus(employeeInfo.getEmployeeId(), StatusType.INACTIVE.getValue());
        response.sendRedirect("/login");
    }
}
