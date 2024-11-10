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
    <script type="text/javascript">
        messaging.requestPermission()
            .then(function () {
                return messaging.getToken(); // FCM 토큰을 요청합니다.
            })
            .then(function (token) {
                console.log(token); // 토큰을 콘솔에 출력합니다.
                localStorage.setItem('Fcmtoken', token); // 토큰을 로컬 스토리지에 저장합니다.
                console.log(localStorage.getItem('Fcmtoken')); // 저장된 토큰을 콘솔에 출력합니다.

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
            })
            .catch(function (error) {
                // 오류 발생 시 콘솔에 오류를 출력합니다.
                console.error('Unable to get permission to notify.', error);
                // 여기서 추가적인 오류 처리 로직을 구현할 수 있습니다.
                // 예를 들어, 사용자에게 오류 메시지를 표시하거나, 특정 기능의 접근을 제한할 수 있습니다.
            });
        setTimeout(function() {
            window.location.href = "/chatrooms";
        }, 1500);
    </script>


    <title>Insert title here</title>
</head>
<body>

</body>

</html>
