<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ include file="/WEB-INF/views/component/lib.jsp" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title>kcc정보통신 | 비밀번호 찾기</title>
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
            등록했던 외부 메일 주소를 입력해 주세요.
          </div>
          <form id="password-form" action="/find-password/email" method="post">
            <div id="input1">
              외부 메일 주소 :
              <input type="email" name="email" id="externalEmail" placeholder="user@example.com" required />
            </div>
            <div id="checkedId">
              <!-- danger code -->
            </div>

            <input
              type="hidden"
              name="${_csrf.parameterName}"
              value="${_csrf.token}"
            />
          </form>
          <div class="buttons-email">
            <button id="find-password-email-button" type="submit">다음</button>
            <button id="find-password-email-backpage">뒤로가기</button>
          </div>
        </div>
      </div>
    </div>
  </body>
  <script src="<%= request.getContextPath() %>/static/user/find-password.js"></script>
  <script src="<%= request.getContextPath() %>/static/user/find-password-empty-check.js"></script>
</html>
