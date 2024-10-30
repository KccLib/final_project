<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ include file="/WEB-INF/views/component/lib.jsp" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title>kcc정보통신 | 일정</title>
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/schedule/schedule.css"
    />
    <script src="<%= request.getContextPath() %>/static/component/fullcalendar/dist/index.global.js"></script>
    <script src="<%= request.getContextPath() %>/static/component/fullcalendar/dist/index.global.js"></script>
  </head>

  <body>
    <jsp:include page="/WEB-INF/views/component/top-bar.jsp"></jsp:include>
    <div class="layout-container">
      <jsp:include
        page="/WEB-INF/views/component/first-side-bar.jsp"
      ></jsp:include>
      <div id="contents">
        <div
          id="popMenu"
          class="list-group"
          style="display: none; width: 150px"
        >
          <a
            href=""
            class="list-group-item list-group-item-action"
            id="right-button-for-delete"
            >⛔ 삭제하기</a
          >
        </div>
        <div id="add-schedule">
          <div id="add-schedule-button">
            <span><i class="fa-solid fa-calendar-plus"></i> 일정 등록</span>
          </div>
        </div>
        <div id="name-bar">
          <div id="employee-name-location">
            <security:authorize access="isAuthenticated()">
              ${name} 님의 일정
            </security:authorize>
          </div>
        </div>
        <!-- <div id="calendar-bar">
        <input type="month" id="date-picker" />
        <button id="today-button"></button>
        <button id="left-button"><</button>
        <button id="right-button">></button>
      </div> -->

        <div id="calendar"></div>
        <!-- 일정등록 모달 -->
        <div id="add-schedule-container" class="hidden">
          <div class="add-schedule-content">
            <div id="title-add-sche">일정 등록</div>
            <form id="schedule-form">
              <div class="add-sche-text">
                <div id="add-schedule-name"><i class="fa-solid fa-pen-to-square"></i>&nbsp;일정명</div>
                <input
                  type="text"
                  id="schedule-name"
                  name="schedule-name"
                  placeholder="일정명을 입력해주세요."
                  required
                />
              </div>

              <div class="add-sche-date">
                <label for="start-date"><i class="fa-regular fa-calendar-check"></i>&nbsp;시작일</label>
                <input
                  type="text"
                  placeholder="날짜를 선택해주세요."
                  id="start-date"
                  name="start-date"
                  required
                />

                <label for="end-date" id="end-date-label"><i class="fa-regular fa-calendar-check"></i> 종료일</label>
                <input
                  type="text"
                  placeholder="날짜를 선택해주세요."
                  id="end-date"
                  name="end-date"
                  required
                />

                <label
                  ><input type="checkbox" name="allDayCheck" id="allDayCheck" />
                  하루 종일
                </label>
              </div>

              <div id="invite">
                <div id="invite-title">
                  <div id="title-text">
                    <i class="fa-solid fa-user-clock"></i>&nbsp;인원 초대 - 총원
                    <div id="count-employees"><span id="count"></span>명</div>
                  </div>
                  <input type="text" name="employees[]" />
                </div>
                <div class="checkbox-group">
                  <label>
                    <input
                      type="checkbox"
                      id="email-alram"
                      name="email-alram"
                      class="checkbox"
                    />
                    이메일 알림
                  </label>

<%--                  <label id="messenger-alram-label">--%>
<%--                    <input--%>
<%--                      type="checkbox"--%>
<%--                      id="messenger-alram"--%>
<%--                      name="messenger-alram"--%>
<%--                      class="checkbox"--%>
<%--                    />--%>
<%--                    메신저 알림--%>
<%--                  </label>--%>
                </div>
              </div>

              <div id="wyswyg">
                <div id="schedule-contents-title"><i class="fa-solid fa-clipboard"></i>&nbsp;일정내용</div>
                <div id="schedule-contents"></div>
              </div>
              <div id="add-schedule-modal-buttons">
                <button type="submit" id="submit-add-schedule">
                  일정 추가
                </button>
                <button id="close-button">닫기</button>
              </div>
              <input
                type="hidden"
                name="${_csrf.parameterName}"
                value="${_csrf.token}"
              />
            </form>
            <span id="close-button"></span>
          </div>
        </div>
        <div id="detail-container" class="hidden">
          <div id="detail-contents">
            <div id="detail-inner">
              <div id="detail-title"></div>
              <div id="schedule-master">
                주최자 :
                <input
                  type="text"
                  value=""
                  id="master-name"
                  name="master-date"
                  readonly
                />
              </div>
              <div class="detail-dates">
                <label for="start-date">시작일:</label>
                <input
                  type="text"
                  value=""
                  id="start-date-detail"
                  name="start-date"
                  readonly
                />

                <label for="end-date" id="end-date-label">종료일:</label>
                <input
                  type="text"
                  id="end-date-detail"
                  value="2024-10-02"
                  name="end-date"
                  readonly
                />
              </div>
              <div class="add-people">
                <div id="add-people-text">참석 인원</div>
                <div id="add-people-table"></div>
              </div>
              <div class="detail-text">
                <div id="detail-text-title" data-schduleid="1">일정 내용</div>
                <!-- 임시 Quill 컨테이너 -->
                <div id="temp-quill-container" style="display: none"></div>
                <div id="detail-text-contents"></div>
              </div>
              <div id="detail-buttons">
                <button id="detail-modify" class="hidden">일정 수정</button>
                <button id="detail-delete">일정 삭제</button>
                <button id="detail-close">닫기</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script src="<%= request.getContextPath() %>/static/schedule/schedule.js"></script>
  </body>
</html>
