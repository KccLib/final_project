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


</body>
<script src="<%= request.getContextPath() %>/static/user/detail.js"></script>

</html>
