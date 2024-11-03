<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %> <%@ include file="/WEB-INF/views/component/lib.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>kcc정보통신</title>
    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/user/detail.css"
    />
</head>
<body>
   <jsp:include page="/WEB-INF/views/component/top-bar.jsp"></jsp:include>
    <div class="layout-container">
    <jsp:include page="/WEB-INF/views/component/first-side-bar.jsp"></jsp:include>

    <div id="employee-detail-container">
        <div id="employee-detail-contents" >
            <div id="detail-employee-profile-info">
                <div id="detail-profile-img">
                    <input type="file" id="fileInput" accept="image/*" style="display: none;">
                    <img
                      src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
                      alt="Profile Image"
                      width="150"
                      height="150"
                    />
                    <div id="detail-profile-img-modify"><i class="fa-solid fa-camera"></i></div>

                </div>
                <div id="detail-user-profile">
                    <div id="detail-user-name">김길동 <p id="detail-user-position">사원</p></div>
                    <div id="detail-user-dept">인사과</div>
                    <div id="detail-user-email">exam@exam.com</div>
                </div>
            </div>
            <form id="detail-modify-form" action="/employees/modify" >
                <div class="detail-infos" id="detail-infos-top">
                    <i class="fa-solid fa-location-dot"></i>
                    <label for="detail-locate">사내위치 &nbsp; </label><input id="detail-locate" >
                </div>
                <div class="detail-infos" >
                    <i class="fa-solid fa-envelope"></i>
                    <label for="detail-external-email" >외부 이메일주소  &nbsp; </label><input id="detail-external-email"  style="width: 17rem;">
                </div>
                <div class="detail-infos">
                    <i class="fa-solid fa-square-phone"></i>
                    <label for="detail-phone-number">전화번호  &nbsp; </label><input id="detail-phone-number" >
                </div>
                <div class="detail-infos">
                    <i class="fa-solid fa-print"></i>
                    <label for="detail-fax-number">팩스번호 &nbsp; </label><input id="detail-fax-number" >
                </div>
                <div class="detail-infos">
                    <i class="fa-solid fa-square-check"></i>
                    <label for="detail-password-check">비밀번호 확인 &nbsp;</label><input id="detail-password-check" style="width: 18rem;">
                </div>
                <div id="detail-modify-buttons">
                    <button id="password-modify">비밀번호 변경</button>
                    <button type="submit" id="modify-apply">저장</button>
                    <button id="employee-detail-close">닫기</button>
                </div>
            </form>
        </div>
    </div>

    </div>
</body>
<script src="<%= request.getContextPath() %>/static/user/detail.js"></script>
</html>
