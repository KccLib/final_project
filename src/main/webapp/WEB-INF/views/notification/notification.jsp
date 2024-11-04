<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib
        prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/component/lib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/notification/notification.css"
    />
    <script src="/static/notification/notification.js" charset="utf-8"></script>

    <title>Insert title here</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/component/top-bar.jsp"/>
<div class="layout-container">
    <jsp:include page="/WEB-INF/views/component/first-side-bar.jsp"/>
    <div class="second-side-bar-container">
    <div class="second-side-bar">
        <div class="container-fluid">
            <div
                    class="row category d-flex justify-content-between d-flex align-items-center"
            >
                <div class="col-4">
                    <p class="category-name">알림</p>
                </div>
            </div>
            <div class="notification-list">
                <c:forEach items="${notifications}"
                           var="notification">
                    <div class="row notification-item justify-content-between align-items-center" data-notification-id="${notification.notificationId}" data-schedule-id="${notification.relatedId}">
                    <div class="col-3">
                        <i class="fa-regular fa-calendar-check"></i>
                    </div>
                    <div class="col-9">
                        <div class="row w-100 justify-content-between">
                            <div class="col-8 no-padding-left notification-title">
                                <p>${notification.title}</p>
                            </div>
                            <div class="col-3 notification-time">
                               <p>${notification.writeDt}</p>
                            </div>
                        </div>
                        <div class="row notification-contents w-100">
                            <div class="col-12">
                                <p>${notification.contents}</p>
                            </div>
                        </div>
                    </div>
                </div>
                </c:forEach>
            </div>
        </div>
    </div>
    </div>
    <div class="contents-list">
    <div class="contents default-contents">
        <div class="container-fluid">
            <div class="default-area">
                <div class="row justify-content-center align-items-center" style="height: 80vh;">
                    <div class="col-12 text-center">
                        <img src="https://s3.ap-northeast-2.amazonaws.com/img.stibee.com/23010_1599648318.png" alt="채팅을 선택해주세요" class="placeholder-image" style="width: 600px; border-radius: 10px">
                        <h2 class="mt-4">알림을 선택해주세요</h2>
                        <p class="text-muted mt-2">왼쪽 목록에서 알림을 선택하여 알림을 확인하세요.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="contents notification-contents" style="display: none">
        <div class="container-fluid">
            <div class="detail-container d-flex justify-content-center align-items-center">
                <div class="detail-inner">
                    <div class="row detail-contents">
                        <div class="col-12 detail-title d-flex justify-content-center">
                        </div>
                    </div>
                    <div class="row writer-box">
                        <div class="col-3">
                            <i class="fa-solid fa-user-tie"></i>
                            주최자
                            <input
                                    type="text"
                                    value="SI영업 1팀/김길동 부장"
                                    id="writer-detail"
                                    name="writer"
                                    class="ml-1"
                                    readonly
                            />
                        </div>
                    </div>
                    <div class="row detail-dates justify-content-start">
                        <div class="start-date-box col-6">
                            <i class="fa-regular fa-calendar-check"></i>
                            <label for="start-date">시작일</label>
                            <input
                                    type="text"
                                    value="2024-10-02"
                                    id="start-date-detail"
                                    name="start-date"
                                    class="ml-1"
                                    readonly
                            />
                        </div>
                        <div class="col-6 d-flex justify-content-end align-items-center">
                            <i class="fa-regular fa-calendar-check"></i>
                            &nbsp;
                            <label for="end-date" id="end-date-label">종료일 </label>
                            <input
                                    type="text"
                                    id="end-date-detail"
                                    value="2024-10-02"
                                    name="end-date"
                                    class="ml-1"
                                    readonly
                            />
                        </div>
                    </div>
                    <div class="row add-people">
                        <div id="add-people-text" class="col-12">
                            <i class="fa-solid fa-user-clock"></i>
                            참석 인원
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="notification-table">
                                <div id="table-container">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12 detail-text">
                            <div id="detail-text-title" data-schduleid="1">
                                <i class="fa-solid fa-clipboard"></i>
                                일정 내용
                            </div>
                            <!-- 임시 Quill 컨테이너 -->
                            <div id="temp-quill-container" style="display: none"></div>
                            <div id="detail-text-contents"></div>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col-4 d-flex justify-content-center">
                            <div id="detail-buttons" style="width: 100%;">
                                <button id="detail-approve" class="col-6">수락</button>
                                <button id="detail-reject" class="col-6">거절</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>
</body>

</html>
