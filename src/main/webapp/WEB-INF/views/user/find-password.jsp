<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ include file="/WEB-INF/views/component/lib.jsp" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title>kcc정보통신 | login</title>
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/user/find-password.css"
    />
  </head>
  <body>
    <div id="full">
      <div id="find-password-container">
        <!-- main contents -->
        <div id="find-password-contents">
          <div id="logo">
            <img
              src="<%= request.getContextPath() %>/static/component/kcc-logo-no-title.png"
            />
          </div>
          <div id="find-password-title">비밀번호 찾기</div>
          <div id="find-password-message">
            임시 비밀번호를 받으려면 아이디를 입력해주세요.
          </div>
          <form id="password-form" action="/find-password/id" method="post">
            <div id="input1">
              아이디 :
              <input
                id="employeeId"
                type="email"
                name="email"
                placeholder="(예시: user@example.com)"
                required
              />
            </div>
            <div id="checkedId">
              <!-- 아이디 검증 이후 실패 시 추가 -->
            </div>

            <input
              type="hidden"
              name="${_csrf.parameterName}"
              value="${_csrf.token}"
            />
          </form>
          <div class="buttons">
            <button id="find-password-button" type="submit">다음</button>
            <button id="find-password-close">닫기</button>
          </div>
        </div>
      </div>
    </div>
  </body>
  <script src="<%= request.getContextPath() %>/static/user/find-password.js"></script>
</html>
