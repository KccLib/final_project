<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ include file="/WEB-INF/views/component/lib.jsp" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/chat_room/chat-room.css"
    />
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/chat_room/chat-room-save.css"
    />
    <script src="/static/chat_room/chat-room-save.js" charset="utf-8"></script>

    <!-- 소스 다운 -->
    <script src="https://unpkg.com/@yaireo/tagify"></script>

    <title>Insert title here</title>
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/component/top-bar.jsp" />
    <jsp:include page="/WEB-INF/views/component/first-side-bar.jsp" />

    <div class="second-side-bar">
      <div>
        <div class="container-fluid">
          <div
            class="row category d-flex justify-content-between d-flex align-items-center"
          >
            <div class="col-4">
              <p class="category-name">채팅</p>
            </div>
            <div class="col-3">
              <a href="http://localhost:8081/chatrooms/save">
                <i class="fa-regular fa-pen-to-square"></i>
              </a>
            </div>
          </div>
          <div class="row chat-room-type">
            <div class="col-6 selection-box">
              <div class="d-flex justify-content-center chat-room-selection">
                <p>목록</p>
              </div>
            </div>
            <div class="col-6 favor-box inactive-box">
              <div class="d-flex justify-content-center chat-room-favor">
                <p>즐겨찾기</p>
              </div>
            </div>
          </div>
          <div class="chat-rooms-list">
            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row chat-room mt-3">
              <div class="col-3">
                <div class="profile">
                  <img
                    src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                  />
                  <div
                    class="status d-flex justify-content-center align-items-center"
                  >
                    <i class="fa-solid fa-check check-icon"></i>
                  </div>
                </div>
              </div>
              <div class="col-8 d-flex align-content-between flex-wrap">
                <div class="row d-flex justify-content-between">
                  <div class="col-8">
                    <div class="chat-room-name">
                      <p>채팅방 이름</p>
                    </div>
                  </div>
                  <div class="col-2">
                    <div class="last-message-at sub-text">
                      <p>14:30</p>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="chat-room-last-message sub-text mb-3">
                      <p>마지막 메시지</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="contents">
      <div class="container-fluid">
        <div class="row category d-flex align-items-center">
          <div class="col-4"><p class="category-name">채팅 생성</p></div>
        </div>
        <form
          name="create-chatroom-form"
          action="http://localhost:8081/chatrooms/save"
          method="post"
        >
          <div class="input-box">
            <div class="add-emp-box">
              <div class="row add-dept-box">
                <i class="fa-solid fa-plus plus-icon"></i>
                <span>조직도로 추가하기</span>
              </div>
              <div class="row input-add-emp-box">
                <div class="col-1">
                  <i class="fa-solid fa-user-plus user-plus-icon"></i>
                </div>
                <div class="col-11">
                  <input
                    name="employees[]"
                    placeholder="사용자를 추가해주세요."
                    value=""
                    data-blacklist=".NET,PHP"
                    autofocus
                  />
                </div>
              </div>
            </div>
            <div class="input-add-group-name">
              <div class="row">
                <div class="col-1">
                  <i class="fa-solid fa-pen-to-square"></i>
                </div>
                <div class="col-11">
                  <input name="chatRoomName" type="text" name="chatRoomName" placeholder="채팅방 명을 입력해주세요." />
                </div>
              </div>
            </div>
            <div class="button-area">
              <div class="row d-flex justify-content-end">
                <div class="col-4">
                  <div class="row d-flex justify-content-between">
                    <div class="col-6">
                      <button type="submit" class="create-button">생성</button>
                    </div>
                    <div class="col-6">
                      <button class="cancel-button" onclick="history.back();">취소</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  </body>

  <script>

  </script>
</html>
