package com.kcc.trioffice.global.handler;

import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.global.enums.StatusType;
import com.kcc.trioffice.global.exception.type.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final EmployeeMapper employeeMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 후 리디렉션 처리
        String redirectUrl = "/token";
        request.getSession().setMaxInactiveInterval(60 * 60 * 8); // 세션 유지 시간 설정 (8시간)

        EmployeeInfo employeeInfo = employeeMapper.getEmployeeInfoFindByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("해당 직원이 존재하지 않습니다."));
        employeeMapper.changeEmployeeStatus(employeeInfo.getEmployeeId(), StatusType.ACTIVE.getValue());

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            redirectUrl = "/admin/main"; // ROLE_ADMIN일 경우 관리자 페이지로 리디렉션
        }
        response.sendRedirect(redirectUrl); // 로그인 성공 후 리디렉션
    }
}
