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

    <!-- 소스 다운 -->
    <script src="https://unpkg.com/@yaireo/tagify"></script>
    <!-- 폴리필 (구버젼 브라우저 지원) -->
    <script src="https://unpkg.com/@yaireo/tagify/dist/tagify.polyfills.min.js"></script>
    <link
            href="https://unpkg.com/@yaireo/tagify/dist/tagify.css"
            rel="stylesheet"
            type="text/css"
    />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"></script>

    <script type="text/javascript">
        var initialChatRoomId = "${initialChatRoomId}";
        initialChatRoomId = parseInt(initialChatRoomId);

        messaging.requestPermission()
            .then(function () {
                return messaging.getToken(); // FCM 토큰을 요청합니다.
            })
            .then(function (token) {
                console.log(token); // 토큰을 콘솔에 출력합니다.
                localStorage.setItem('Fcmtoken', token); // 토큰을 로컬 스토리지에 저장합니다.
                console.log(localStorage.getItem('Fcmtoken')); // 저장된 토큰을 콘솔에 출력합니다.
            })
            .catch(function (error) {
                // 오류 발생 시 콘솔에 오류를 출력합니다.
                console.error('Unable to get permission to notify.', error);
                // 여기서 추가적인 오류 처리 로직을 구현할 수 있습니다.
                // 예를 들어, 사용자에게 오류 메시지를 표시하거나, 특정 기능의 접근을 제한할 수 있습니다.
            });

        $.ajax({
            url: '/api/employees/fcm-token',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({fcmToken: localStorage.getItem('Fcmtoken')}),
            success: function (data) {
                console.log('FCM을 저장하는데에 성공했습니다.');
            },
            error: function (xhr, status, error) {
                console.error('FCM을 저장하는데에 실패했습니다.:', error);
            }
        });
    </script>

    <title>Insert title here</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/component/top-bar.jsp"/>
<div class="layout-container">
<jsp:include page="/WEB-INF/views/component/first-side-bar.jsp"/>

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
                    <div
                            class="status d-flex justify-content-center align-items-center"
                    >
                        <i class="fa-solid fa-check check-icon"></i>
                    </div>
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
            </div>
            <div class="chat-send-box">
                <div class="container-fluid">
                    <div class="d-flex justify-content-center">
                        <div class="chat-send-input d-flex justify-content-center">
                            <input type="text" class="chat-input" placeholder="메시지를 입력하세요."/>
                            <i class="fa-regular fa-file"></i>
                            <i id="send" class="fa-regular fa-paper-plane"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="file-area" style="display: none;">
            <div class="tag-input-area">
                <div class="row">
                    <div class="col-1 d-flex align-items-center justify-content-end">
                        <i class="fa-solid fa-tag"></i>
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
                    <div class="col-3">
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                태그
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="#">태그</a></li>
                                <li><a class="dropdown-item" href="#">파일명</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="file-table">
                <div class="table-container">
                    <table>
                        <thead>
                        <tr>
                            <th>파일명</th>
                            <th>공유 날짜</th>
                            <th>보낸 사람</th>
                            <th>태그</th>
                            <th>다운로드</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="image-area" style="display: none;">
            <div class="tag-input-area">
                <div class="row">
                    <div class="col-1 d-flex align-items-center justify-content-end">
                        <i class="fa-solid fa-tag"></i>
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
                    <div class="col-3">
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                태그
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="#">태그</a></li>
                                <li><a class="dropdown-item" href="#">파일명</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="images">
                <div class="row">
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="image-box">
                            <img src="https://www.jwps.co.kr/images/nose/gp/03_01_bg_m.jpg">
                        </div>
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
