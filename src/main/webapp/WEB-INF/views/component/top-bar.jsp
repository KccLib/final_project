<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ include file="/WEB-INF/views/component/lib.jsp" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title></title>
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/component/reset.css"
    />
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/component/top-bar.css"
    />
  </head>

  <body>
    <div class="top-bar">
      <div class="logo">
        <a href="/"
          ><img
            src="<%= request.getContextPath() %>/static/component/kcc-logo.png"
        /></a>
      </div>
      <!-- 검색창 -->
      <div class="search">
        <input type="text" id="search-bar" placeholder="사원 및 채팅방 검색" />
        <img
          src="https://s3.ap-northeast-2.amazonaws.com/cdn.wecode.co.kr/icon/search.png"
        />
      </div>
      <!-- 사용자 -->
      <div class="employee" id="employee-icon">
        <i class="fa-solid fa-user fa-2x"></i>
      </div>
      <!-- 사용자 상태 조회 모달 -->
      <div id="modalContainer" class="hidden">
        <div id="modalContent">
          <div id="logout">
            <form
              id="logoutForm"
              action="/logout"
              method="post"
              style="display: inline"
            >
              <input
                type="hidden"
                name="${_csrf.parameterName}"
                value="${_csrf.token}"
              />
              <a href="#" id="logoutLink">로그아웃</a>
            </form>
          </div>
          <div id="profile-img">
            <img
              src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
              alt="Profile Image"
              width="150"
              height="150"
            />
            <div id="profile-img-modify"><i class="fa-solid fa-pen"></i></div>
          </div>
          <div id="user-profile">
            <div id="user-name">김길동 대리</div>
            <div id="user-dept">SI영업 1팀</div>
            <div id="user-email">gildongkim@kcc.co.kr</div>
          </div>
          <div id="user-status-box">
            <div id="status"><i class="fa-solid fa-check"></i></div>
            <div id="status-text">대화 가능</div>
            <div id="status-modify-icon">
              <i class="fa-solid fa-chevron-right"></i>
            </div>
          </div>
          <div id="status-container" class="hidden">
            <div id="status-ok">
              <div id="status-ok-icon"><i class="fa-solid fa-check"></i></div>
              <span>대화가능</span>
            </div>
            <div id="status-reset">
              <div id="status-reset-icon">
                <i class="fa-solid fa-minus"></i>
              </div>
              <span>자리비움</span>
            </div>
            <div id="status-offline">
              <div id="status-offline-icon">
                <i class="fa-solid fa-minus"></i>
              </div>
              <span>오프라인</span>
            </div>
            <div id="status-disturb">
              <div id="status-disturb-icon">
                <i class="fa-solid fa-minus"></i>
              </div>
              <span>방해금지</span>
            </div>
          </div>
          <div id="status-message">
            <div id="status-message-contents">
              [SI영업] <br />
              국민건강보험공단 영업 담당<br />
              인터넷 진흥원 영업 담당
            </div>

            <div id="status-message-buttons">
              <div id="message-pen"><i class="fa-solid fa-pen"></i></div>
              <div id="message-trash">
                <i class="fa-solid fa-trash-can"></i>
              </div>
            </div>
          </div>
          <div id="buttons">
            <button id="modify-status">정보 수정</button>
            <button id="modalCloseButton">닫기</button>
          </div>
        </div>
      </div>
    </div>

    <div id="search-bar-container" class="hidden">
      <div id="search-bar-contents" >
        <div id="search-people-title">사원</div>
        <div id="search-people-contents">
          <div class="search-peoples">
            <div class="search-profile-img">
              1231
            </div>
            <p class="search-profile-name">홍길동</p>
            <p class="search-profile-dept">금융1팀</p>
          </div>
          <div class="search-peoples">people1</div>
          <div class="search-peoples">people1</div>
          <div class="search-peoples">people1</div>
          <div class="search-peoples">people1</div>
          </div>
        <div id="search-group-title">그룹 채팅</div>
        <div class="search-group-contents">
          <div class="search-chat-profile-img">
          </div>
          <p class="search-chatroom-name">그룹채팅방</p>
        <div class="search-group-contents">123123</div>
        <div class  ="search-group-contents">123123</div>
      </div>

    </div>
  </body>

  <script src="<%= request.getContextPath() %>/static/component/top-bar.js"></script>
</html>
