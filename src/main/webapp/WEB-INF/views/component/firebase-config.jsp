<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/component/lib.jsp" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval var="apiKey" expression="@environment.getProperty('fcm.apiKey')" />
<spring:eval var="authDomain" expression="@environment.getProperty('fcm.authDomain')" />
<spring:eval var="projectId" expression="@environment.getProperty('fcm.projectId')" />
<spring:eval var="storageBucket" expression="@environment.getProperty('fcm.storageBucket')" />
<spring:eval var="messagingSenderId" expression="@environment.getProperty('fcm.messagingSenderId')" />
<spring:eval var="appId" expression="@environment.getProperty('fcm.appId')" />
<spring:eval var="measurementId" expression="@environment.getProperty('fcm.measurementId')" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>kcc정보통신 | Notification Test</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-messaging.js"></script>
    <script>
        const firebaseConfig = {
            apiKey: "${apiKey}",
            authDomain: "${authDomain}",
            projectId: "${projectId}",
            storageBucket: "${storageBucket}",
            messagingSenderId: "${messagingSenderId}",
            appId: "${appId}",
            measurementId: "${measurementId}",
        };

        // Firebase 초기화
        firebase.initializeApp(firebaseConfig);

        if ('serviceWorker' in navigator) {
            navigator.serviceWorker.register('/firebase-messaging-sw.js')
                .then(function(registration) {
                    console.log('Service Worker registration successful with scope: ', registration.scope);
                })
                .catch(function(err) {
                    console.log('Service Worker registration failed: ', err);
                });
        }
        const messaging = firebase.messaging();

        messaging.onMessage(function(payload) {
            console.log('Message received. ', payload);
            // 알림 표시
            const data = payload.data;
            const notificationTitle = data.title;
            const notificationOptions = {
                body: data.body,
                icon: data.profileUrl || '/default-icon.png',
            };

            // 페이지가 활성화된 상태에서는 브라우저 알림을 직접 표시해야 합니다.
            new Notification(notificationTitle, notificationOptions);
        });

    </script>
</head>
<body>
</body>
</html>