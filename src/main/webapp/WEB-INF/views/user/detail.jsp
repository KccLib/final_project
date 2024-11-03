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
            <div id="detail-employee-title">회원 정보</div>
            <div id="detail-employee-profile-info">
                <div id="detail-profile-img">
                    <input type="file" id="fileInput" accept="image/*" style="display: none;">
                    <img
                      src="${employee.profileUrl}"
                      alt="Profile Image"
                      width="150"
                      height="150"
                    />
                    <div id="detail-profile-img-modify"><i class="fa-solid fa-camera"></i></div>

                </div>
                <div id="detail-user-profile">
                    <div id="detail-user-name">${employee.name} <p id="detail-user-position">${employee.position}</p></div>
                    <div id="detail-user-dept">${employee.deptName}</div>
                    <div id="detail-user-email">${employee.email}</div>
                </div>
            </div>
            <form id="detail-modify-form" action="/employees/modify" method="POST" >
                <div class="detail-infos" id="detail-infos-top">
                    <i class="fa-solid fa-location-dot"></i>
                    <label for="detail-locate">사내위치 &nbsp; </label><input id="detail-locate" value="${employee.location}" placeholder="사내 위치를 입력하세요">
                </div>
                <div class="detail-infos" id="external-email-container">
                    <i class="fa-solid fa-envelope"></i>
                    <label for="detail-external-email" >외부 이메일주소  &nbsp; </label><input id="detail-external-email" value="${employee.externalEmail}" style="width: 19rem;" placeholder="외부 이메일 주소를 입력하세요">
                </div>
                <div class="detail-infos" id="phone-number-container" >
                    <i class="fa-solid fa-square-phone"></i>
                    <label for="detail-phone-number">전화번호  &nbsp; </label><input id="detail-phone-number" value="${employee.phoneNum}" placeholder="전화번호를 입력하세요 (예: 010-1234-5678)">
                </div>
                <div class="detail-infos" id="fax-number-container">
                    <i class="fa-solid fa-print"></i>
                    <label for="detail-fax-number">팩스번호 &nbsp; </label><input id="detail-fax-number" value="${employee.fax}" placeholder="팩스번호를 입력하세요 (예: 02-123-4567)">
                </div>
                <div class="detail-infos" id="password-check-container">
                    <i class="fa-solid fa-square-check"></i>
                    <label for="detail-password-check">비밀번호 확인 &nbsp;</label><input id="detail-password-check" placeholder="8~15자 특수문자, 대소 영문자로 작성해주세요." style="width: 20rem;" required>
                </div>
            </form>
            <div id="detail-modify-buttons">
                    <button id="password-modify">비밀번호 변경</button>
                    <button type="submit" id="modify-apply" disabled>저장</button>
                    <button id="employee-detail-close">닫기</button>
                </div>
        </div>
    </div>

    </div>
</body>
<script src="<%= request.getContextPath() %>/static/user/detail.js"></script>
</html>
