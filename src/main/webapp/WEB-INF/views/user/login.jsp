<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ include file="/WEB-INF/views/component/lib.jsp" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title>kcc정보통신 | login</title>
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/user/login.css"
    />
  </head>
  <body>
    <%-- <jsp:include page="/WEB-INF/views/component/top-bar.jsp"></jsp:include>
    --%>

    <div class="full">
      <div class="login-box">
        <div id="logo">
          <img
            src="<%= request.getContextPath() %>/static/component/kcc-logo-no-title.png"
          />
        </div>
        <div id="company-name"><span>KCC 정보통신</span></div>

        <form id="login-form" action="/login" method="post">
          <div id="input1">
            <input type="email" placeholder="아이디(메일)" name="id" required />
          </div>

          <div id="input2">
            <input
              type="password"
              placeholder="비밀번호"
              name="password"
              required
            />
          </div>
          <div id="checkedId">
            <% if (request.getParameter("error") != null) { %>
            <div class="error-message">
              아이디나 비밀번호가 일치하지 않습니다.
            </div>
            <% } %>
          </div>
          <button id="login-button" class="btn-primary" type="submit">
            로그인
          </button>
          <input
            type="hidden"
            name="${_csrf.parameterName}"
            value="${_csrf.token}"
          />
        </form>

        <div id="login-options">
          <label id="remember-label">
            <input type="checkbox" name="remember-check" id="remember-check" />
            <span style="position: relative; top: -2px">아이디 저장하기</span>
          </label>
          <div class="bar">|</div>
          <div id="find-id">
            <a>아이디 찾기</a>
          </div>

          <div class="bar">|</div>
          <div id="find-password">
            <a href="/find-password">비밀번호 찾기</a>
          </div>
        </div>
      </div>
      <!-- 아이디찾기 모달 -->
      <div id="find-id-container" class="hidden">
        <div id="find-id-contents">
          <div id="logo">
            <img
              src="<%= request.getContextPath() %>/static/component/kcc-logo-no-title.png"
            />
          </div>
          <div id="find-id-title">아이디 찾기</div>
          <div id="find-id-text">
            인사과 담당자에게 문의 부탁드립니다.<br />
            <br />
            담당자 : <span id="find-id-employee"></span><br />
            Tel.
            <span id="find-id-employee-tel"></span>
          </div>
          <button id="find-id-close">닫기</button>
        </div>
      </div>
    </div>
  </body>
  <script src="<%= request.getContextPath() %>/static/user/login.js"></script>
</html>
