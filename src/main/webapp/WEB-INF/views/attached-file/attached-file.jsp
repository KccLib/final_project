<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %> <%@ include
file="/WEB-INF/views/component/lib.jsp" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="stylesheet"
      href="<%= request.getContextPath() %>/static/attached-file/attached-file.css"
    />
    <script
      src="/static/attached-file/attached-file.js"
      charset="utf-8"
    ></script>

    <title>Insert title here</title>
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/component/top-bar.jsp" />
    <div class="layout-container">
      <jsp:include page="/WEB-INF/views/component/first-side-bar.jsp" />
      <div class="contents">
        <div class="container-fluid">
          <div class="row justify-content-center search-items-area">
            <div class="col-6">
              <input
                name="search-item"
                placeholder="태그, 검색어를 입력해주세요."
                class="tagify--outside"
                autofocus
              />
            </div>
          </div>
          <div class="row justify-content-center button-list">
            <div>
              <div class="dropdown">
                <button
                  class="btn btn-secondary dropdown-toggle"
                  type="button"
                  id="dropdownMenuButton1"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  <i class="fa-regular fa-file filter-icon"></i>
                  &nbsp;<span class="filter-title">유형</span>
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                  <li>
                    <a class="dropdown-item" href="#" data-extension="img">
                      <i class="fa-regular fa-image"></i>&nbsp; img
                    </a>
                  </li>
                  <li>
                    <a class="dropdown-item" href="#" data-extension="pdf">
                      <i class="fa-solid fa-file-pdf"></i>&nbsp; PDF
                    </a>
                  </li>
                  <li>
                    <a class="dropdown-item" href="#" data-extension="xlsx">
                      <i class="fa-solid fa-table"></i>&nbsp; Excel
                    </a>
                  </li>
                  <li>
                    <a class="dropdown-item" href="#" data-extension="ppt">
                      <i class="fa-solid fa-file-powerpoint"></i>&nbsp; PPT
                    </a>
                  </li>
                  <li>
                    <a class="dropdown-item" href="#" data-extension="zip" style="margin-bottom: 0px;">
                      <i class="fa-solid fa-file-zipper"></i>&nbsp; ZIP
                    </a>
                  </li>
                  <li><hr class="dropdown-divider" /></li>
                  <li>
                    <a class="dropdown-item" href="#" data-extension="" style="margin-bottom: 0px;">
                      <i class="fa-regular fa-file"></i>&nbsp; 전체
                    </a>
                  </li>
                </ul>
              </div>
            </div>
            <div>
              <div class="dropdown">
                <button
                  class="btn btn-secondary dropdown-toggle"
                  type="button"
                  id="dropdownMenuButton2"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  <i class="fa-regular fa-user"></i>
                  &nbsp;<span class="filter-title">사람</span>
                </button>
                <ul
                  class="dropdown-menu search-people-dropdown"
                  aria-labelledby="dropdownMenuButton2"
                >
                  <li>
                    <div class="input-group">
                      <input
                        id="search-employee"
                        type="text"
                        class="form-control"
                        placeholder="보낸 사람을 검색하세요."
                        aria-label="Recipient's username"
                        aria-describedby="button-addon2"
                      />
                    </div>
                  </li>
                  <div class="search-people-list">
                  </div>
                  <li><hr class="dropdown-divider" /></li>
                  <li>
                    <a class="dropdown-item" href="#" data-extension="" style="margin-bottom: 0px;">
                      <i class="fa-regular fa-user"></i>&nbsp; 전체
                    </a>
                  </li>
                </ul>
              </div>
            </div>
            <div>
              <div class="dropdown">
                <button
                  class="btn btn-secondary dropdown-toggle"
                  type="button"
                  id="dropdownMenuButton3"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  <i class="fa-regular fa-calendar"></i>
                  &nbsp;<span class="filter-title">일자</span>
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton3">
                  <li><a class="dropdown-item" href="#">오늘</a></li>
                  <li><a class="dropdown-item" href="#">지난 7일</a></li>
                  <li>
                    <a class="dropdown-item" href="#">지난 30일</a>
                  </li>
                  <li>
                    <a class="dropdown-item" href="#" style="margin-bottom: 0px;">올해</a>
                  </li>
                  <li><hr class="dropdown-divider" /></li>
                  <li>
                    <a class="dropdown-item" href="#" style="margin-bottom: 0px;">
                      <i class="fa-regular fa-calendar"></i>&nbsp; 전체
                    </a>
                  </li>
                </ul>
              </div>
            </div>
            <div>
              <div>
                <button type="button" class="btn" id="resetButton" style="border-radius: 10px; background: #ff7c7c; color: white;">
                <i class="fa-solid fa-rotate-right"></i>
                  <span class="filter-title">초기화</span>

</button>
              </div>
            </div>
          </div>
          <div class="row file-title">
            <div class="col-12">
              <p>첨부파일</p>
            </div>
          </div>
          <div class="row file-table">
            <div class="col-12">
              <table class="table">
                <thead>
                  <tr>
                    <th scope="col" style="width: 30%">이름</th>
                    <th scope="col" style="width: 15%">보낸 사람</th>
                    <th scope="col" style="width: 15%">채팅방 위치</th>
                    <th scope="col" style="width: 10%">보낸 일자</th>
                    <th scope="col" style="width: 20%">태그</th>
                    <th scope="col" style="width: 10%">다운로드</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach
                    items="${attachedFileDetailInfos}"
                    var="attachedFileDetailInfo"
                  >
                    <tr>
                      <th style="width: 30%">
                        <p class="file-name">
                          <c:choose>
                            <c:when
                              test="${empty attachedFileDetailInfo.fileExtension}"
                            >
                              <i class="fa-solid fa-file"></i>
                            </c:when>
                            <c:when
                              test="${fn:toLowerCase(attachedFileDetailInfo.fileExtension) eq 'pdf'}"
                            >
                              <i class="fa-solid fa-file-pdf"></i>
                            </c:when>
                            <c:when
                              test="${fn:toLowerCase(attachedFileDetailInfo.fileExtension) eq 'png'}"
                            >
                              <i class="fa-solid fa-file-image"></i>
                            </c:when>
                            <c:when
                              test="${fn:toLowerCase(attachedFileDetailInfo.fileExtension) eq 'csv'}"
                            >
                              <i class="fa-solid fa-file-csv"></i>
                            </c:when>
                            <c:when
                              test="${fn:toLowerCase(attachedFileDetailInfo.fileExtension) eq 'doc'}"
                            >
                              <i class="fa-solid fa-file-word"></i>
                            </c:when>
                            <c:when
                              test="${fn:toLowerCase(attachedFileDetailInfo.fileExtension) eq 'zip'}"
                            >
                              <i class="fa-solid fa-file-zipper"></i>
                            </c:when>
                            <c:when
                              test="${fn:toLowerCase(attachedFileDetailInfo.fileExtension) eq 'mp4'}"
                            >
                              <i class="fa-solid fa-file-video"></i>
                            </c:when>
                            <c:otherwise>
                              <i class="fa-solid fa-file"></i>
                            </c:otherwise>
                          </c:choose>

                          ${attachedFileDetailInfo.fileName}
                        </p>
                      </th>
                      <td style="width: 15%">
                        <div class="row align-items-center">
                          <img
                            src="${attachedFileDetailInfo.senderProfileUrl}"
                          />
                          &nbsp;${attachedFileDetailInfo.senderName}
                        </div>
                      </td>
                      <td style="width: 15%">
                        <p class="chat-room-name">
                          ${attachedFileDetailInfo.chatRoomName}
                        </p>
                      </td>
                      <td style="width: 10%">
                        ${attachedFileDetailInfo.writeDt}
                      </td>
                      <td style="width: 20%">
                        <c:if test="${not empty attachedFileDetailInfo.tags}">
                          <c:forEach
                            items="${attachedFileDetailInfo.tags}"
                            var="tag"
                          >
                            <span class="tag">${tag}</span>
                          </c:forEach>
                        </c:if>
                      </td>
                      <td style="width: 10%">
                        <i class="fa-solid fa-download"></i>
                      </td>
                    </tr>
                  </c:forEach>
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
      </div>
    </div>
  </body>
</html>
