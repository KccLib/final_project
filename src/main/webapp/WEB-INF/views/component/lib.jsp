<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="security"
uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title></title>

    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/component/main-contents.css"
    />

    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/component/second-side-bar.css"
    />
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/component/main-contents.css"
    />
    <!-- bootstrap -->
    <link
      href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css"
      rel="stylesheet"
    />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"></script>

    <!-- 폰트어썸 -->
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
      integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
      crossorigin="anonymous"
    />

    <!-- material_icons -->
    <link
      href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet"
    />

    <%-- drop zone --%>
    <link
      rel="stylesheet"
      href="https://unpkg.com/dropzone@5/dist/min/dropzone.min.css"
      type="text/css"
    />
    <script src="https://unpkg.com/dropzone@5/dist/min/dropzone.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <!-- sweet alert -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <!-- mobiscroll -->
    <link
      href="<%= request.getContextPath() %>/static/component/mobiscroll/mobiscroll.javascript.min.css"
      rel="stylesheet"
    />
    <script src="<%= request.getContextPath() %>/static/component/mobiscroll/mobiscroll.javascript.min.js"></script>

    <!-- Quill  -->
    <link
      href="https://cdn.quilljs.com/1.3.6/quill.snow.css"
      rel="stylesheet"
    />
    <script src="https://cdn.quilljs.com/1.3.6/quill.js"></script>

    <!-- tagify -->
    <!-- 소스 다운 -->
    <!-- <script src="https://unpkg.com/@yaireo/tagify"></script> -->
    <script src="https://unpkg.com/@yaireo/tagify"></script>
    <!-- 폴리필 (구버젼 브라우저 지원) -->
    <script src="https://unpkg.com/@yaireo/tagify/dist/tagify.polyfills.min.js"></script>
    <link href="https://unpkg.com/@yaireo/tagify/dist/tagify.css" rel="stylesheet" type="text/css" />

    <%-- FCM --%>
    <script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-messaging.js"></script>
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/component/reset.css"
    />
    <link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-square.css" rel="stylesheet">

    <%-- markdown --%>
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>

    <script src="<%= request.getContextPath() %>/static/component/common.js"></script>

    <style>
      /* NanumSquare_ac 폰트 정의 */
      @font-face {
        font-family: "NanumSquare_ac";
        src: url(https://hangeul.pstatic.net/hangeul_static/webfont/NanumSquare/NanumSquareR.eot);
        src: url(https://hangeul.pstatic.net/hangeul_static/webfont/NanumSquare/NanumSquareR.eot?#iefix) format("embedded-opentype"), url(https://hangeul.pstatic.net/hangeul_static/webfont/NanumSquare/NanumSquareR.woff) format("woff"), url(https://hangeul.pstatic.net/hangeul_static/webfont/NanumSquare/NanumSquareR.ttf) format("truetype");
        font-weight: normal;
        font-style: normal;
      }

      /* One Mobile Title 폰트 정의 */
      @font-face {
        font-family: 'One Mobile Title';
        src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_2105_2@1.0/ONE-Mobile-Title.woff') format('woff');
        font-weight: normal;
        font-style: normal;
      }


      /* 적용방법 */
      .test-font {
        font-family: "NanumSquare_ac", sans-serif;
      }

      .test-title {
        font-family: "One Mobile Title", sans-serif;
        font-weight: 400; /* Regular weight */
      }
    </style>
  </head>
  <body></body>
</html>
