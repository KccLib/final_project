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
    <script src="<%= request.getContextPath() %>/static/component/fullcalendar/dist/index.global.js"></script>
    <script src="<%= request.getContextPath() %>/static/component/fullcalendar/dist/index.global.js"></script>
    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/schedule/schedule.css"
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
            <div id="user-name"></div>
            <div id="user-dept"></div>
            <div id="user-email"></div>
          </div>
          <div id="user-status-box">
                <%--            상태값 동적 변환 --%>
          </div>
          <div id="status-container" class="hidden">
            <div id="status-ok">
              <div class="status-ok-icon"><i class="fa-solid fa-check"></i></div>
              <span style="margin-top: 5px;">대화가능</span>
            </div>
            <div id="status-reset">
              <div class="status-reset-icon">
                <i class="fa-solid fa-minus"></i>
              </div>
              <span>자리비움</span>
            </div>
            <div id="status-offline">
              <div class="status-offline-icon">
                <i class="fa-solid fa-minus"></i>
              </div>
              <span style="margin-top: 5px;">오프라인</span>
            </div>
            <div id="status-disturb">
              <div class="status-disturb-icon">
                <i class="fa-solid fa-minus"></i>
              </div>
              <span style="margin-top: 6px;">방해금지</span>
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

    <%-- 검색바 --%>
    <div id="search-bar-container" class="hidden">
      <div id="search-bar-contents">
        <div id="search-people-title">사원</div>
        <div id="search-people-contents"><%-- 검색된 직원들 js로 추가 --%></div>
        <div id="search-group-title">그룹 채팅</div>
        <div id="search-group-chat-rooms"><%-- 검색된 채팅방 목록 --%></div>
      </div>
    </div>

    <%-- 다른 사용자 조회 --%>
    <div id="other-employee-info-container" class="hidden">
      <div class="layout-container">
        <div class="second-side-bar">
          <div id="employee-info-container">
            <div id="profile-name-position">
              <div id="other-profile-status">
                <div id="other-employee-status"></div>
                <div id="other-employee-img"><%-- 이미지 넣기--%></div>
              </div>
              <div id="other-employee-name-position">
                <p id="other-name">김길동</p>
                &nbsp;
                <p id="other-position">사원</p>
              </div>
            </div>
            <div id="other-etc-info-container">
              <div id="other-dept-container">
                <p id="other-dept">SI 영업 1팀</p>
              </div>
              <div id="other-email-container">
                <p id="other-email">exam01@kcc.co.kr</p>
              </div>
              <div id="other-locate-container">
                <p id="other-locate">동측 기둥 0번자리</p>
              </div>
              <div id="other-contents-container">
                <p id="other-contents">
                  내일은 내일의 해가 뜬다. <br />
                  ~ 2024 03 02 ing
                </p>
              </div>
              <div id="other-chat-button">
                <i class="fa-regular fa-comment"></i>
                <a id="enter-chat-room">대화하기</a>
              </div>
            </div>
          </div>
        </div>
        <div class="contents">
          <div id="close-button-container">
            <div id="other-employee-schedule-close">
              <button id="other-employee-schedule-close-button">닫기</button>
            </div>
          </div>
          <div id="name-bar-top-bar">
            <div id="employee-name-location-top-bar"><%-- js에서 추가 --%></div>
          </div>
          <!-- <div id="calendar-bar">
          <input type="month" id="date-picker" />
          <button id="today-button"></button>
          <button id="left-button"><</button>
          <button id="right-button">></button>
        </div> -->

          <div id="calendar-top-bar"></div>
        </div>
      </div>
    </div>
  </body>

  <script src="<%= request.getContextPath() %>/static/component/top-bar.js"></script>
</html>
