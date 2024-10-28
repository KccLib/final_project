package com.kcc.trioffice.global.config;

import com.kcc.trioffice.domain.employee.mapper.EmployeeMapper;
import com.kcc.trioffice.global.handler.CustomLoginSuccessHandler;
import com.kcc.trioffice.global.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.kcc.trioffice.global.auth.PrincipalDetailService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final EmployeeMapper employeeMapper;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new SimpleUrlAuthenticationFailureHandler("/login?error");
  }

  @Bean
  public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedDoubleSlash(true); // 이중 슬래시 허용
    return firewall;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, PrincipalDetailService principalDetailService)
      throws Exception {
    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/**")
            .requireCsrfProtectionMatcher(request -> {
              String uri = request.getRequestURI();
              // 해당 페이지에 csrf 활성화
              return uri.equals("/login") || uri.equals("/find-password/id") | uri.equals("/find-password/email");
            }))
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN 권한이 있는 사용자만 접근 가능
            .requestMatchers("/schedules/**").authenticated() // 로그인한 사용자만 접근
            .requestMatchers("/notifications/**").authenticated() // 로그인한 사용자만 접근
            .requestMatchers("/chatrooms/**").authenticated() // 로그인한 사용자만 접근
            .anyRequest().permitAll() // 그 외 전부 허가
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .failureHandler(authenticationFailureHandler()) // 로그인 실패 처리
            .usernameParameter("id")
            .passwordParameter("password")
            .successHandler(new CustomLoginSuccessHandler(employeeMapper))
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessHandler(new CustomLogoutSuccessHandler(employeeMapper))
            .logoutSuccessUrl("/login")
            .deleteCookies("JSESSIONID", "remember-me")
            .invalidateHttpSession(true));

    return http.build();
  }
}
