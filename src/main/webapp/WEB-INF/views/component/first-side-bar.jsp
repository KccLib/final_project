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
      href="<%= request.getContextPath() %>/static/component/first-side-bar.css"
    />
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>

  </head>

  <body>
    <div class="side-bar-menu">
      <div class="side-bar-menu-icon-with-name">
        <div class="alram">
          <a
            href="/notifications"
            class="alram-atag"
            style="text-decoration: none"
          >
            <div class="icons"><i class="fa-regular fa-2x fa-bell"></i></div>
            <div class="menu-text">알림</div>
          </a>
        </div>
        <div id="chat">
          <a href="/chatrooms" style="text-decoration: none">
            <div class="icons">
              <i class="fa-regular fa-2x fa-comments"></i>
            </div>
            <div class="menu-text">채팅</div>
          </a>
        </div>
        <div class="date">
          <a href="/schedules" style="text-decoration: none">
            <div class="icons">
              <i class="fa-regular fa-2x fa-calendar"></i>
            </div>
            <div class="menu-text">일정</div>
          </a>
        </div>
        <div class="company">
          <a href="/departments" style="text-decoration: none">
            <div class="icons">
              <i class="fa-solid fa-2x fa-sitemap"></i>
            </div>
            <div class="menu-text">조직도</div>
          </a>
        </div>
      </div>
      <div class="chat-bot">
        <div class="icons" id="bot-icon">
          <i class="fa-brands fa-2x fa-bots"></i>
        </div>
      </div>

      <div id="chat-bot-container" class="hidden">
        <div id="chat-bot-contents">
          <div id="chat-bot-top-bar">
            <div id="chat-bot-title">
              <i class="fa-solid fa-comment-medical"></i> 트리오챗
            </div>
            <div id="chat-bot-right-buttons">
              <div id="chat-bot-return"><i class="fa-solid fa-rotate"></i></div>
              <div id="chat-bot-close"><i class="fa-solid fa-xmark"></i></div>
            </div>
          </div>
          <div id="chat-bot-messages">
            <div class="chat-bot-server">
              <p class="default-bot-message">
고객지원팀입니다. 궁금하신 [현재 상황, 질문]을 
자세히 입력해주세요.😊  
              </p>
            </div>

            
          </div>
          <div id="chat-bot-guide">
            <div id="chat-bot-guide-first">
              <div class="chat-bot-guides" id="chat-bot-guide-1">
                자주하는 질문 👀
              </div>
              <div class="chat-bot-guides" id="chat-bot-guide-2">
                챗봇 답변을 잘받는 TIP❗
              </div>
            </div>
            <div id="chat-bot-guide-second">
              <div class="chat-bot-guides" id="chat-bot-guide-3">
                2024 신규기능 활용 세미나😊
              </div>
              <div class="chat-bot-guides" id="chat-bot-guide-4">
                ✅맞춤법 검사를 부탁해요
              </div>
            </div>
          </div>
          <div id="chat-bot-input-div">
            <textarea
              id="chat-bot-input"
              placeholder="질문을 입력해주세요."
              required
            ></textarea>
            <i class="fa-solid fa-location-arrow"></i>
          </div>
        </div>ㅌ
      </div>
    </div>
    <script src="<%= request.getContextPath() %>/static/component/first-side-bar.js"></script>
  </body>
</html>
