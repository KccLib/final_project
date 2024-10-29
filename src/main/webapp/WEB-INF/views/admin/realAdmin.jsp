<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ include file="/WEB-INF/views/component/lib.jsp" %>

    <!DOCTYPE html>
    <html>

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <link rel="stylesheet" href="<%= request.getContextPath() %>/static/department/adminDepartment.css" />
      <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
      <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" rel="stylesheet">
      <link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-square.css" rel="stylesheet" />
      <title>사용자 조직도</title>

      <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    </head>

    <body>
      <jsp:include page="/WEB-INF/views/component/top-bar.jsp" />
      <jsp:include page="/WEB-INF/views/component/first-side-bar.jsp" />

      <div class="fixed-menu">
        <ul>
          <p>조직도</p>
          <li class="menu">
            <div class="menu2">
              <a>kcc정보통신</a>
            </div>
          </li>

          <li class="menu">
            <div class="menu2">
                <a>대표이사</a>
                <div class="menu3">
                    <i class="fa-solid fa-ellipsis-vertical"></i>
                </div>
            </div>
            <ul class="hide2">
                <li>
                    <div class="employee-info">
                        <div class="profile">
                            <img src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                        </div>
                        <a>대표이사1</a>
                        <div class="ellipsis-icon"><i class="fa-solid fa-ellipsis-vertical"></i></div>
                    </div>
                </li>

                <li>
                                    <div class="employee-info">
                                        <div class="profile">
                                            <img src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                                        </div>
                                        <a>대표이사2</a>
                                        <div class="ellipsis-icon"><i class="fa-solid fa-ellipsis-vertical"></i></div>
                                    </div>
                                </li>


            </ul>
        </li>
        



          <!-- 추가 부서들 -->
          <li class="menu">
            <div class="menu2">
              <a>시스템통합(SI)</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group">SI영업1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    SI영업1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    SI영업1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">SI영업2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    SI영업2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    SI영업2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">SI영업3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    SI영업3팀 부서원1
                  </li>
                  <li class="employee-item" data-employee-name="SI영업3팀 부서원2" data-dept-emp="SI영업3팀 사원"
                    data-email="example@kcc1.co.co">
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    SI영업3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>

          <!-- 아웃소싱 부서 -->
          <li class="menu">
            <div class="menu2" id="ito-department">
              <a>아웃소싱(ITO)</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group" id="ito-team">아웃소싱 1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    아웃소싱1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    아웃소싱1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">아웃소싱2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    아웃소싱2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    아웃소싱2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">아웃소싱3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    아웃소싱3팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    아웃소싱3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>

          <!-- 신사업 부서 -->
          <li class="menu">
            <div class="menu2">
              <a>신사업</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group">신사업 1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    신사업1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    신사업1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">신사업2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    신사업2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    신사업2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">신사업3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    신사업3팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    신사업3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>

          <!-- 보안 부서 -->
          <li class="menu">
            <div class="menu2">
              <a>보안</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group">보안1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    보안1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    보안1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">보안2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    보안2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    보안2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">보안3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    보안3팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    보안3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>

          <!-- 인프라 부서 -->
          <li class="menu">
            <div class="menu2">
              <a>인프라</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group">인프라1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    인프라1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    인프라1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">인프라2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    인프라2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    인프라2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">인프라3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    인프라3팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    인프라3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>

          <!-- 팩키지 솔루션 부서 -->
          <li class="menu">
            <div class="menu2">
              <a>팩키지솔루션</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group">팩키지솔루션1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    팩키지솔루션1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    팩키지솔루션1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">팩키지솔루션2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    팩키지솔루션2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    팩키지솔루션2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">팩키지솔루션3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    팩키지솔루션3팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    팩키지솔루션3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>

          <!-- RPA 부서 -->
          <li class="menu">
            <div class="menu2">
              <a>RPA</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group">RPA1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    RPA1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    RPA1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">RPA2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    RPA2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    RPA2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">RPA3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    RPA3팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    RPA3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>

          <!-- 사업제휴 부서 -->
          <li class="menu">
            <div class="menu2">
              <a>사업제휴</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group">사업제휴1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    사업제휴1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    사업제휴1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">사업제휴2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    사업제휴2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    사업제휴2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">사업제휴3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    사업제휴3팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    사업제휴3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>

          <!-- 부서 미지정 -->
          <li class="menu">
            <div class="menu2">
              <a>부서 미지정</a>
            </div>
            <ul class="hide">
              <li>
                <div class="group">부서 미지정1팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    부서 미지정1팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    부서 미지정1팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">부서 미지정2팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    부서 미지정2팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    부서 미지정2팀 부서원2
                  </li>
                </ul>
              </li>
              <li>
                <div class="group">부서 미지정3팀</div>
                <ul class="hide2">
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    부서 미지정3팀 부서원1
                  </li>
                  <li>
                    <div class="profile">
                      <img
                        src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
                    </div>
                    부서 미지정3팀 부서원2
                  </li>
                </ul>
              </li>
            </ul>
          </li>
        </ul>
      </div>
      </div>



      <div class="contents">
        <div class="contents-1-1">
          <div class="dept-sub">시스템통합(SI) > SI영업3팀</div>
          <div class="img2">
            <img src="<%= request.getContextPath() %>/static/component/kcc정보통신.png" />
          </div>
          <div class="contents-1">
            <div class="profile2">
              <img
                src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png" />
            </div>
            <div class="employee-impl">
              <p class="employee-name">우영두 사원</p>
              <p class="dept-emp">SI영업3팀 사원</p>
              <p class="email">yd12@kcc1.co.co</p>

              <div class="comment-calendar">
                <a href="#" class="chat-room-go">
                  <i class="fa-regular fa-comment"></i>
                  <p style="padding-top: 10px; font-weight: bold;">대화하기</p>
                </a>
                <a href="#" class="comment-icon">
                  <i class="fa-solid fa-calendar-days"></i>
                  <p style="padding-top: 10px; font-weight: bold;">일정 확인하기</p>
                </a>
              </div>
            </div>
          </div>

          <div class="contents-2">
            <div class="info-labels">
              <p>회사</p>
              <p>부서</p>
              <p style="padding-top: 40px;">직위</p>
              <p>직통전화</p>
              <p>핸드폰번호</p>
            </div>
            <div class="info-values">
              <p>(주)KCC정보통신</p>
              <p>SI영업3팀</p>
              <p class="sub-info">시스템통합(SI) > SI영업3팀</p>
              <p>사원</p>
              <p>02-1234-5678</p>
              <p>010-1234-5678</p>
            </div>
          </div>
        </div>
      </div>

      <script>
        $(document).ready(function () {
          // dept-sub, contents-1, contents-2를 처음에 모두 숨기기
          $(".dept-sub").hide();
          $(".contents-1").hide();
          $(".contents-2").hide();

          // 전역 변수를 추가하여 menu2의 텍스트 저장
          var currentMenuText = ""; // 현재 클릭된 menu2의 텍스트

          // 메뉴 클릭 시 active 클래스 토글 및 슬라이드 효과 처리
          $(".menu2").click(function () {
            var parentMenu = $(this).closest(".menu");
            currentMenuText = $(this).text().trim(); // 클릭된 메뉴의 텍스트

            // 클릭된 메뉴에 'active' 클래스가 이미 있으면 제거
            if (parentMenu.hasClass("active")) {
              parentMenu.removeClass("active"); // 메뉴 비활성화
              $(this).css("background-color", ""); // 클릭된 메뉴의 배경색 원상복구
              parentMenu.children("ul").slideUp(); // 바로 하위 메뉴 숨기기

              // dept-sub 텍스트 제거
              $('.dept-sub').text(""); // dept-sub 비우기

              // dept-sub가 비어있으면 모든 active 요소를 none으로 변경
              if ($('.dept-sub').text().trim() === "") {
                $(".menu").each(function () {
                  if ($(this).hasClass("active")) {
                    $(this).removeClass("active");
                    $(this).children("ul").slideUp();
                  }
                });

                // contents-1과 contents-2 숨기기
                $(".contents-1").hide();
                $(".contents-2").hide();
                $(".dept-sub").hide();
              }
            } else {
              // 다른 메뉴의 'active' 클래스 및 배경색 제거
              $(".menu").removeClass("active");
              $(".menu2").css("background-color", ""); // 모든 메뉴 배경색 원상복구
              $(".menu ul").slideUp(); // 모든 하위 메뉴 숨기기

              // 클릭된 메뉴에 'active' 클래스 추가 및 배경색 변경
              parentMenu.addClass("active");
              $(this).css("background-color", "rgba(0, 123, 255, 0.5)"); // 클릭된 메뉴만 배경색 변경
              parentMenu.children("ul").slideDown(); // 바로 하위 메뉴만 보이기

              // dept-sub 업데이트
              $('.dept-sub').text(currentMenuText); // dept-sub에 클릭된 메뉴의 텍스트 추가

              // dept-sub가 비어있으면 contents-1과 contents-2 숨기기
              if ($('.dept-sub').text().trim() === "") {
                $(".contents-1").hide();
                $(".contents-2").hide();
                $(".dept-sub").hide();
              }
            }
          });

          // group 클래스를 가진 div 클릭 시
          $(".group").click(function () {
            var newText = $(this).text().trim(); // 클릭된 div의 텍스트 가져오기

            // dept-sub 클래스에서 현재 텍스트 가져오기
            var currentDeptSub = $('.dept-sub').text().trim(); // 올바른 방법으로 현재 텍스트 가져오기

            // 현재 dept-sub이 비어있거나, 클릭한 텍스트가 다르면 새로운 텍스트로 변경
            if (currentDeptSub === "" || currentDeptSub.split(' > ').slice(-1)[0] !== newText) {
              if (currentDeptSub) {
                // menu2 텍스트 추가
                $('.dept-sub').text(currentMenuText + " > " + newText); // dept-sub에 새 텍스트 추가
              } else {
                $('.dept-sub').text(currentMenuText + " > " + newText); // dept-sub에 처음 추가
              }

              // SI영업3팀 p 요소 업데이트
              $(".info-values p").eq(1).text(newText);
              $(".dept-emp").text(newText); // dept-emp 업데이트
              $(".sub-info").text(newText); // sub-info p 태그 업데이트
            } else {
              // 클릭한 그룹이 dept-sub에 이미 있을 경우, 제거
              $('.dept-sub').text(currentMenuText); // menu2 텍스트만 남기고 그룹 제거
            }

            // dept-sub가 비어있으면 contents-1과 contents-2 숨기기
            if ($('.dept-sub').text().trim() === "") {
              $(".contents-1").hide();
              $(".contents-2").hide();
              $(".dept-sub").hide();
            } else {
              $(".contents-1").show(); // dept-sub에 텍스트가 있을 경우 contents-1 보이기
              $(".contents-2").show(); // dept-sub에 텍스트가 있을 경우 contents-2 보이기
              $(".dept-sub").show();
            }
          });

          // 하위 메뉴 클릭 시 슬라이드 효과 처리
          $(".menu ul li").click(function (event) {
            var subSubMenu = $(this).children("ul");
            if (subSubMenu.is(":visible")) {
              subSubMenu.slideUp(); // 하위의 하위 메뉴 숨기기
            } else {
              subSubMenu.slideDown(); // 하위의 하위 메뉴 보이기
            }
            event.stopPropagation(); // 클릭 이벤트 전파 방지
          });

          // hide2 클래스의 li 클릭 시 모든 기능 실행
          $(".hide2 li").click(function (event) {
            var employeeName = $(this).text().trim(); // 클릭된 li의 텍스트를 가져옴
            $('.employee-name').text(employeeName); // employee-name 업데이트

            // 클릭된 li의 상위 메뉴를 찾아 active 처리
            var parentMenu = $(this).closest(".menu"); // li의 부모 메뉴 찾기
            var menuItem = $(this).closest(".menu2"); // 클릭된 li의 부모 메뉴 찾기

            // 메뉴가 활성화되지 않았다면
            if (!parentMenu.hasClass("active")) {
              // 다른 메뉴의 'active' 클래스 및 배경색 제거
              $(".menu").removeClass("active");
              $(".menu2").css("background-color", ""); // 모든 메뉴 배경색 원상복구
              $(".menu ul").slideUp(); // 모든 하위 메뉴 숨기기

              // 클릭된 메뉴에 'active' 클래스 추가 및 배경색 변경
              parentMenu.addClass("active");
              menuItem.css("background-color", "rgba(0, 123, 255, 0.5)"); // 클릭된 메뉴만 배경색 변경
              parentMenu.children("ul").slideDown(); // 바로 하위 메뉴만 보이기

              // 클릭된 메뉴의 텍스트를 가져와 dept-sub 업데이트
              var newText = menuItem.text().trim(); // 클릭된 메뉴의 텍스트
              $('.dept-sub').text(newText); // dept-sub 업데이트
            }

            // hide2 li 클릭 시 contents-1과 contents-2 보이기
            $(".contents-1").show();
            $(".contents-2").show();
            $(".dept-sub").show();

            event.stopPropagation(); // 클릭 이벤트 전파 방지
          });
        });


      </script>

    </body>

    </html>