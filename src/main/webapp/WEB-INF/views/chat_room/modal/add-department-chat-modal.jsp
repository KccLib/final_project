<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Insert title here</title>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.15/jstree.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.15/themes/default/style.min.css" />
    <link
            rel="stylesheet"
            href="<%= request.getContextPath() %>/static/chat_room/modal/add-department-chat-modal.css"
    />
    <%--    js 추가--%>
    <script src="/static/chat_room/chat-room-save.js" charset="utf-8"></script>
</head>
<body>
<div class="modal fade" id="add-department-chat-modal" tabindex="-1" aria-labelledby="imageModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="imageModalLabel">조직도로 추가하기</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div id="jstree"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="add-department-employee" class="btn btn-primary" id="select-add-employee">선택</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>