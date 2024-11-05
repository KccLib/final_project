<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %> <%@ include file="/WEB-INF/views/component/lib.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>kcc정보통신</title>
    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/user/change-password.css"
    />
</head>
<body>
   <jsp:include page="/WEB-INF/views/component/top-bar.jsp"></jsp:include>
    <div class="layout-container">
    <jsp:include page="/WEB-INF/views/component/first-side-bar.jsp"></jsp:include>
        <div id="change-password-container">
            <div id="change-password-contents">
                <div id="change-password-title">비밀번호 변경</div>

                <div id="password-check-container">
                    <div id="existing-password">
                        <i class="fa-solid fa-key"></i>
                        <label for="existing-password-input">기존 비밀번호 &nbsp;</label>
                        <input type="password" id="existing-password-input" name="password" placeholder="8~15자 특수문자, 대소 영문자로 작성해주세요." style="width: 20rem;" required>
                        <span class="watch-password" id="first-password"><i  class="fa-solid fa-eye"></i></span>
                    </div>

                    <div id="change-password">
                        <i class="fa-solid fa-key"></i>
                        <label for="change-password-input">새 비밀번호 &nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="password" id="change-password-input" name="password" placeholder="8~15자 특수문자, 대소 영문자로 작성해주세요." style="width: 20rem;" required>
                        <span class="watch-password" id="second-password"><i  class="fa-solid fa-eye"></i></span>
                    </div>

                    <div id="change-password-check">
                        <i class="fa-solid fa-key"></i>
                        <label for="change-password-check-input">비밀번호 확인 &nbsp;</label>
                        <input type="password" id="change-password-check-input" name="password" placeholder="8~15자 특수문자, 대소 영문자로 작성해주세요." style="width: 20rem;" required>
                        <span class="watch-password" id="third-password"><i  class="fa-solid fa-eye"></i></span>
                    </div>

                    <div id="change-password-button-container">
                        <button id="change-password-button" disabled>저장</button>
                        <button id="close-change-password">닫기</button>
                    </div>
                </div>

            </div>
        </div>
    </div>
</body>
<script src="<%= request.getContextPath() %>/static/user/change-password.js"></script>

</html>
