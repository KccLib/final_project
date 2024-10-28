package com.kcc.trioffice.global.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.global.exception.type.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

  private final EmployeeMapper employeeMapper;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

    Optional<EmployeeInfo> employee = employeeMapper.getEmployeeInfoFindById(id);

    if (employee.isPresent()) {
      return new PrincipalDetail(employee.get());
    } else {
      throw new NotFoundException("일치하는 아이디가 없습니다.");
    }

  }

}
