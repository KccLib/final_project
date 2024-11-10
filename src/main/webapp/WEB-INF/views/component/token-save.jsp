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

    <title>Insert title here</title>
</head>
<body>
<div id="loading-spinner" style="display: none;">
    <div class="spinner"></div>
    <div>로딩 중입니다...</div>
</div>
<script type="text/javascript">
    // 로딩 스피너를 표시합니다.
    document.getElementById('loading-spinner').style.display = 'flex';
    var loadingStartTime = Date.now(); // 로딩 시작 시간

    messaging.requestPermission()
        .then(function () {
            return messaging.getToken();
        })
        .then(function (token) {
            console.log('FCM 토큰:', token);
            localStorage.setItem('Fcmtoken', token);

            // 서버에 FCM 토큰 저장 요청
            $.ajax({
                url: '/api/employees/fcm-token',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({fcmToken: token}),
                success: function (data) {
                    console.log('FCM 토큰 저장 성공');
                    // 최소 로딩 시간 계산
                    var minimumLoadingTime = 1500; // 최소 로딩 시간 (밀리초)
                    var elapsedTime = Date.now() - loadingStartTime;
                    var remainingTime = minimumLoadingTime - elapsedTime;

                    if (remainingTime > 0) {
                        // 남은 시간만큼 대기 후 페이지 이동
                        setTimeout(function() {
                            document.getElementById('loading-spinner').style.display = 'none';
                            window.location.href = "/chatrooms";
                        }, remainingTime);
                    } else {
                        // 즉시 페이지 이동
                        document.getElementById('loading-spinner').style.display = 'none';
                        window.location.href = "/chatrooms";
                    }
                },
                error: function (xhr, status, error) {
                    console.error('FCM 토큰 저장 실패:', error);
                    document.getElementById('loading-spinner').style.display = 'none';
                    alert('FCM 토큰 저장에 실패했습니다. 네트워크 상태를 확인해주세요.');
                    window.location.href = "/chatrooms";
                }
            });
        })
        .catch(function (error) {
            console.error('알림 권한 요청 실패 또는 오류 발생:', error);
            document.getElementById('loading-spinner').style.display = 'none';
            alert('알림 권한이 필요합니다. 설정에서 알림 권한을 허용해주세요.');
            window.location.href = "/chatrooms";
        });
</script>

</body>

</html>
