<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib
        prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/component/lib.jsp" %>
<%@ include file="/WEB-INF/views/component/firebase-config.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/chat_room/chat-room.css"
    />
    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/chat_room/chat-room-list.css"
    />
    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/chat_room/chat-room-save.css"
    />
    <%--    js 추가--%>
    <script src="/static/chat_room/chat-room.js" charset="utf-8"></script>
    <script src="/static/chat_room/chat-room-save.js" charset="utf-8"></script>

    <link
            href="https://unpkg.com/@yaireo/tagify/dist/tagify.css"
            rel="stylesheet"
            type="text/css"
    />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"></script>

    <script type="text/javascript">
        var initialChatRoomId = "${initialChatRoomId}";
        initialChatRoomId = parseInt(initialChatRoomId);
    </script>

    <title>Insert title here</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/component/top-bar.jsp"/>
<div class="layout-container">
<jsp:include page="/WEB-INF/views/component/first-side-bar.jsp"/>
<div class="second-side-bar-container">
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
                    <a href="#">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </a>
                </div>
            </div>
            <div class="row chat-room-type">
                <div class="col-6 selection-box">
                    <div class="d-flex justify-content-center selection">
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
                <c:forEach items="${chatRoomList}"
                           var="chatRoom">
                    <div class="row chat-room chat-room-item justify-content-between" data-chat-room-id="${chatRoom.chatRoomId}" data-is-favorited="${chatRoom.isFavorited}">
                        <div class="col-3">
                            <div class="profile">
                                <img
                                        src="${chatRoom.chatRoomProfileImageUrl}"
                                />
                                <c:choose>
                                    <c:when test="${chatRoom.employeeStatus == null}">
                                    </c:when>
                                    <c:when test="${chatRoom.employeeStatus == 1}">
                                        <div class="status d-flex justify-content-center align-items-center">
                                            <i class="fa-solid fa-check check-icon"></i>
                                        </div>
                                    </c:when>
                                    <c:when test="${chatRoom.employeeStatus == 2}">
                                        <div class="absent-status d-flex justify-content-center align-items-center">
                                            <i class="fa-solid fa-minus"></i>
                                        </div>
                                    </c:when>
                                    <c:when test="${chatRoom.employeeStatus == 3}">
                                        <div class="inactive-status d-flex justify-content-center align-items-center">
                                            <i class="fa-solid fa-minus"></i>
                                        </div>
                                    </c:when>
                                    <c:when test="${chatRoom.employeeStatus == 4}">
                                        <div class="dnd-status d-flex justify-content-center align-items-center">
                                            <i class="fa-solid fa-minus"></i>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="status d-flex justify-content-center align-items-center">
                                            <i class="fa-solid fa-question"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col-8 d-flex align-content-between flex-wrap no-padding-left ">
                            <div class="row d-flex justify-content-between name-time-box">
                                <div class="col-8 no-padding-left">
                                    <div class="chat-room-name">
                                        <p>${chatRoom.chatRoomName}</p>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="last-message-at sub-text">
                                        <p>${chatRoom.lastMessageTime}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="row w-100 d-flex justify-content-between align-items-end">
                                <div class="col-9 no-padding-left">
                                    <div class="chat-room-last-message sub-text">
                                        <p>${chatRoom.lastMessage}</p>
                                    </div>
                                </div>

                                <c:if test="${chatRoom.unreadMessageCount > 0}">
                                    <div class="col-3 text-end">
                                        <div class="unread-count-box">
                                            <p class="unread-count">${chatRoom.unreadMessageCount}</p>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</div>

<div class="contents-list">
<div class="contents save-contents" style="display: none">
    <div class="container-fluid">
        <div class="row category d-flex align-items-center">
            <div class="col-4"><p class="category-name">채팅 생성</p></div>
        </div>
        <form
                name="create-chatroom-form"
                action="/chatrooms/save"
                method="post"
        >
            <div class="create-input-box">
                <div class="create-add-emp-box">
                    <div class="row add-dept-box">
                        <div class="add-dept-button">
                            <i class="fa-solid fa-plus plus-icon"></i>
                            <span>조직도로 추가하기</span>
                        </div>
                    </div>
                    <div class="row create-input-add-emp-box">
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
                                    class="add-employee-input"
                                    required
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
                            <input name="chatRoomName" type="text" placeholder="채팅방 명을 입력해주세요." />
                        </div>
                    </div>
                </div>
                <div class="button-area">
                    <div class="row d-flex justify-content-end">
                        <div class="col-4">
                            <div class="row d-flex justify-content-between">
                                <div class="col-6" style="padding-right: 0px;">
                                    <button type="submit" class="create-button">생성</button>
                                </div>
                                <div class="col-6" style="padding-right: 0px;">
                                    <button type="button" class="cancel-button">취소</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="contents default-contents">
    <div class="container-fluid">
        <div class="default-area">
            <div class="row justify-content-center align-items-center" style="height: 80vh;">
                <div class="col-12 text-center">
                    <img src="https://ouch-cdn2.icons8.com/6syYW4lbbu5TDy1nWCp6l-QJtDGMNcQd6WOIK2LtyH8/rs:fit:608:456/extend:false/wm:1:re:0:0:0.8/wmid:ouch/czM6Ly9pY29uczgu/b3VjaC1wcm9kLmFz/c2V0cy9zdmcvNzQ2/L2MwMmE0ODFjLWEx/ZGQtNGM0My04MGQ4/LWE3OGE0NmJiOGY4/Yy5zdmc.png" alt="채팅을 선택해주세요" class="placeholder-image">
                    <h2 class="mt-4">채팅을 선택해주세요</h2>
                    <p class="text-muted mt-2">왼쪽 목록에서 채팅방을 선택하여 대화를 시작하세요.</p>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="contents chat-contents" style="display: none;">
    <div class="container-fluid">
        <div
                class="row category d-flex align-items-center d-flex justify-content-between chat-room-selection"
        >
            <div class="col-1">
                <div class="chat-room-profile">
                    <img
                            src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png"
                            class="chat-room-profile-image"
                    />
                </div>
            </div>
            <div class="col-4">
                <div class="chat-room-name">
                    <p class="group-name"></p>
                </div>
            </div>
            <div
                    class="col-2 d-flex justify-content-center d-flex justify-content-between selection"
            >
                <div class="active">
                    <a href="#" class="chat-button">채팅</a>
                </div>
                <div>
                    <a href="#" class="file-button">파일</a>
                </div>
                <div>
                    <a href="#" class="image-button">사진</a>
                </div>
            </div>
            <div class="col-3"></div>
            <div
                    class="col-2 d-flex justify-content-center d-flex align-items-center"
            >
                <div class="emp-count-box">
                    <i class="fa-solid fa-user-group"></i>
                    <span class="emp-count ml-2">3</span>
                </div>
            </div>
        </div>
        <div class="chat-area" style="display: none;">
            <div class="chat">
                <div class="chat-container">
                </div>
            </div>
            <div class="chat-send-box">
                <div class="container-fluid">
                    <div class="d-flex justify-content-center">
                        <div class="chat-send-input d-flex justify-content-center">
                            <div class="input-container">
                                <textarea class="chat-input" placeholder="메시지를 입력하세요."></textarea>
                                <i class="fa-regular fa-file attach-icon"></i>
                                <i id="send" class="fa-regular fa-paper-plane send-icon"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="file-area" style="display: none;">
            <div class="tag-input-area">
                <div class="row">
                    <div class="col-1">
                        <div class="btn-group">
                            <button type="button" class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                태그
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="#">태그</a></li>
                                <li><a class="dropdown-item" href="#">파일명</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-8">
                        <input
                                name="tags"
                                placeholder="조건에 따라 파일을 검색해보세요."
                                value=""
                                data-blacklist=".NET,PHP"
                                autofocus
                        />
                    </div>
                    <div class="col-2">
                        <div class="btn-group">
                            <button type="button" class="btn select-button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-magnifying-glass" style="font-size: 14px;"></i>
                                조회
                            </button>
                        </div>
                    </div>

                </div>
            </div>
            <div class="row file-table">
            <div class="col-12">
              <table class="table">
                <thead>
                  <tr>
                    <th scope="col" style="width: 35%">이름</th>
                    <th scope="col" style="width: 15%">보낸 사람</th>
                    <th scope="col" style="width: 10%">보낸 일자</th>
                    <th scope="col" style="width: 30%">태그</th>
                    <th scope="col" style="width: 10%">다운로드</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <th style="width: 30%">
                      <i class="fa-solid fa-file-pdf"></i>
                      인사과 정리 자료.pdf
                    </th>
                    <td style="width: 15%">
                      <div class="row align-items-center">
                        <img
                          src="https://www.chosun.com/resizer/v2/DGKOZQCCQVBIHHS5GC7F7FQBKI.png?auth=cdba743fb5a8d14385151c684dcb6be4b6cd73aeb00331bcf184fe2861eca42b&width=616"
                        />
                        &nbsp;우영두
                      </div>
                    </td>
                    <td style="width: 15%">인사과 단체톡방</td>
                    <td style="width: 10%">2024-10-11</td>
                    <td style="width: 20%"><span class="tag">서류</span></td>
                    <td style="width: 10%">
                      <i class="fa-solid fa-download"></i>
                    </td>
                  </tr>
                  <tr>
                    <th style="width: 30%" scope="row">
                      <i class="fa-solid fa-table"></i>
                      인사과 정리 자료.csv
                    </th>
                    <td style="width: 15%">
                      <div class="row align-items-center">
                        <img
                          src="https://www.chosun.com/resizer/v2/DGKOZQCCQVBIHHS5GC7F7FQBKI.png?auth=cdba743fb5a8d14385151c684dcb6be4b6cd73aeb00331bcf184fe2861eca42b&width=616"
                        />
                        <span>&nbsp;Jacob</span>
                      </div>
                    </td>
                    <td style="width: 15%">Thornton</td>
                    <td style="width: 10%">@fat</td>
                    <td style="width: 20%">Otto</td>
                    <td style="width: 10%">
                      <i class="fa-solid fa-download"></i>
                    </td>
                  </tr>
                  <tr>
                    <th style="width: 30%" scope="row">
                      <i class="fa-regular fa-image"></i>
                      인사과 단체 사진
                    </th>
                    <td style="width: 15%" colspan="2">
                      <div class="row align-items-center">
                        <img
                          src="https://www.chosun.com/resizer/v2/DGKOZQCCQVBIHHS5GC7F7FQBKI.png?auth=cdba743fb5a8d14385151c684dcb6be4b6cd73aeb00331bcf184fe2861eca42b&width=616"
                        />
                        &nbsp;Larry the Bird
                      </div>
                    </td>
                    <td style="width: 15%">@twitter</td>
                    <td style="width: 10%">Otto</td>
                    <td style="width: 20%">Otto</td>
                    <td style="width: 10%">
                      <i class="fa-solid fa-download"></i>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div class="image-area" style="display: none;">
            <div class="tag-input-area">
                <div class="row">
                    <div class="col-1">
                        <div class="btn-group">
                            <button type="button" class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                태그
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="#">태그</a></li>
                                <li><a class="dropdown-item" href="#">파일명</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-8">
                        <input
                                name="imageTags"
                                placeholder="조건에 따라 사진을 검색해보세요."
                                value=""
                                data-blacklist=".NET,PHP"
                                autofocus
                        />
                    </div>

                    <div class="col-2">
                        <div class="btn-group">
                            <button type="button" class="btn select-button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-magnifying-glass" style="font-size: 14px;"></i>
                                조회
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="images">
                <div class="row">

                </div>
            </div>

        </div>
    </div>
</div>
</div>
</div>
<div id="emoticon-box"
     style="display: none; position: absolute; padding: 5px; border: 1px solid #ccc; background: white;">
    <!-- Example emoticons, replace these with your actual emoticons -->
    <i class="fa-solid fa-check" data-emoticon-type="CHECK"></i>
    <i class="fa-solid fa-heart heart-icon" data-emoticon-type="HEART"></i>
    <i class="fa-solid fa-thumbs-up" data-emoticon-type="THUMBS_UP"></i>
    <i class="fa-solid fa-face-smile" data-emoticon-type="SMILE"></i>
    <i class="fa-solid fa-face-sad-cry" data-emoticon-type="SAD"></i>
</div>
<%@ include file="/WEB-INF/views/chat_room/modal/participant-emp-modal.jsp" %>
<%@ include file="/WEB-INF/views/chat_room/modal/chat-room-dropdown.jsp" %>
<%@ include file="/WEB-INF/views/chat_room/modal/chat-delete-dropdown.jsp" %>
<%@ include file="/WEB-INF/views/chat_room/modal/send-file-modal.jsp" %>
<%@ include file="/WEB-INF/views/chat_room/modal/image-detail-modal.jsp" %>
<%@ include file="/WEB-INF/views/chat_room/modal/add-department-chat-modal.jsp" %>

</body>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
</html>
