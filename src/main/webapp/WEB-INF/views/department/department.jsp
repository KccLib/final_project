<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/component/lib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List" %>
<%@ page import="com.kcc.trioffice.domain.department.dto.response.Department" %>
<%@ page import="com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/department/department.css" />
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" rel="stylesheet">
    <link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-square.css" rel="stylesheet" />
    <title>사용자 조직도</title>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body>
    <jsp:include page="/WEB-INF/views/component/top-bar.jsp" />
    <div class="layout-container">

    <jsp:include page="/WEB-INF/views/component/first-side-bar.jsp" />

    <div class="second-side-bar">
        <ul>
            <p>조직도</p>
            <%
                List<Department> departmentTree = (List<Department>) request.getAttribute("departmentTree");
                for (Department dept : departmentTree) {
                    if (dept.getUpperDeptId() == null) { // upperDeptId가 null인 부서가 최상위 부서
                        out.print(renderDept(dept)); // 최상위 부서만 출력
                    }
                }
            %>
        </ul>
    </div>

    <div class="contents">
        <div class="contents-1-1">
            <div class="dept-sub">
                <span class="top-department"></span> > <span class="sub-department"></span>
            </div>
            <div class="img2">
                <img src="/static/component/kcc-logo.png">
            </div>
            <div class="contents-1">
                <div class="profile2">
                    <img src="https://e7.pngegg.com/pngimages/1/723/png-clipart-graphy-business-portrait-business-people-public-relations.png">
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
            <div class="contents-2" style="display: none;"> <!-- 처음에는 숨김 -->
                <div class="info-labels">
                    <p>회사</p>
                    <p>부서</p>
                    <p style="padding-top: 40px;">직위</p>
                    <p>직통전화</p>
                    <p>핸드폰번호</p>
                </div>
                <div class="info-values">
                    <p class="company-name">(주)KCC정보통신</p>
                    <p class="department-name">SI영업3팀</p>
                    <p class="sub-info">
                        <span class="top-department"></span> > <span class="sub-department"></span>
                    </p>
                    <p class="position">사원</p>
                    <p class="phone-number">02-1234-5678</p>
                    <p class="mobile-number">010-1234-5678</p>
                </div>
            </div>
        </div>
    </div>

</div>







    <script>
       // 상위부서를 클릭했을 때 하위부서가 표시됨
       $(document).on('click', '.menu2 > a', function () {
           var $subDepartments = $(this).parent().siblings('ul.hide');
           if ($subDepartments.length > 0) {
               $subDepartments.slideToggle(); // 하위부서 토글
               $(this).parent().toggleClass('selected'); // 선택된 부서 표시
           }
       });

       // 하위부서를 클릭했을 때 사원 목록이 표시됨
       $(document).on('click', '.group', function (event) {
           event.stopPropagation(); // 상위부서 이벤트 전파 방지

           var $employees = $(this).siblings('ul.hide2'); // 'group' 요소의 형제 요소 중 'ul.hide2' 찾기
           var deptId = $(this).data('dept-id'); // 클릭한 부서의 deptId를 가져옴
           var subDeptName = $(this).text(); // 클릭한 하위 부서의 이름 가져오기
           var topDeptName = $(this).closest('.menu').find('.menu2 a').text(); // 최상위 부서 이름 가져오기

           if ($employees.length > 0) {
               if (!$employees.hasClass('loaded')) {
                   // 'loaded' 클래스가 없는 경우 (아직 사원 정보가 로드되지 않았을 때만 AJAX 호출)
                   console.log("부서 ID " + deptId + "에 대한 사원 정보를 요청합니다.");
                   $.ajax({
                       url: "/departments/" + deptId + "/employees", // 부서 ID에 맞는 사원 정보 요청
                       type: "GET",
                       success: function (data) {
                           console.log("사원 정보를 성공적으로 받았습니다.", data);
                           var employeeList = "";
                           $.each(data, function (index, employee) {
                               employeeList += "<li class='employee-item' data-emp-id='" + employee.employeeId + "'>" +
                                   "<div class='profile'>" +
                                   "<img src='" + employee.profileUrl + "' alt='Profile' />" +
                                   "</div>" +
                                   employee.name + "</li>";
                           });

                           // 사원 목록이 비어있을 경우 메시지 추가
                           if (employeeList === "") {
                               employeeList = "<li>사원이 없습니다</li>";
                           }

                           $employees.html(employeeList); // 사원 리스트를 hide2 안에 출력
                           $employees.addClass('loaded'); // 한 번 로드된 후에는 다시 AJAX 호출 방지
                       },
                       error: function (jqXHR, textStatus, errorThrown) {
                           console.error("사원 정보를 불러오는데 실패했습니다.", textStatus, errorThrown);
                           alert("사원 정보를 불러오는데 실패했습니다.");
                       }
                   });
               }

               $employees.slideToggle(); // 사원 목록 토글
           }

           $(this).toggleClass('selected'); // 선택된 부서 표시

           // 부서 이름 업데이트
           $('.top-department').text(topDeptName); // 최상위 부서 이름 설정
           $('.sub-department').text(subDeptName); // 하위 부서 이름 설정
       });

       // 직원 항목 클릭 시 contents-1-1과 dept-sub 및 contents-2 표시
       $(document).on('click', '.employee-item', function() {
           // contents-1-1, dept-sub, contents-1, contents-2 div를 보이게 함
           $('.contents-1-1').show(); // contents-1-1 보이기
           $('.dept-sub').show(); // dept-sub 보이기
           $('.img2').hide();
           $('.contents-1').show(); // contents-1 보이기 (여기 추가)
           $('.contents-2').show(); // contents-2 보이기 (slideDown으로 부드러운 전환)

           // 클릭한 직원의 정보를 contents-1에 표시
           var employeeId = $(this).data('emp-id');
           var employeeName = $(this).text();

           $.ajax({
               url: "/api/employees/" + employeeId, // 직원 ID에 맞는 정보 요청
               type: "GET",
               success: function(data) {
                   console.log("직원 정보를 성공적으로 받았습니다.", data);
                   // 정보를 contents-1에 표시
                   $('.contents-1 .employee-name').text(data.name); // 직원 이름
                   $('.contents-1 .dept-emp').text(data.position); // 직원의 부서
                   $('.contents-1 .email').text(data.email); // 직원 이메일
                   $('.contents-1 .profile2 img').attr('src', data.profileUrl); // 직원 프로필 이미지


                   // 추가 정보 표시
                   $('.contents-2 .info-values p:eq(0)').text(data.companyName); // 회사 이름
                   $('.contents-2 .info-values p:eq(1)').text(data.deptName); // 부서 이름
                   $('.contents-2 .info-values p:eq(2)').text(data.subDeptName); // 하위 부서 이름
                   $('.contents-2 .info-values p:eq(3)').text(data.position); // 직위
                   $('.contents-2 .info-values p:eq(4)').text(data.phoneNum); // 핸드폰 번호
               },
               error: function(jqXHR, textStatus, errorThrown) {
                   console.error("직원 정보를 불러오는데 실패했습니다.", textStatus, errorThrown);
                   alert("직원 정보를 불러오는데 실패했습니다.");
               }
           });
       });


    </script>

    <%!
        // 부서 및 하위 부서와 부서원 정보를 출력하는 메서드
        public String renderDept(Department dept) {
            StringBuilder sb = new StringBuilder();
            sb.append("<li class=\"menu\">");
            sb.append("<div class=\"menu2\"><a>").append(dept.getDepartmentName()).append("</a></div>");

            // 하위 부서가 있는 경우만 출력
            List<Department> subDepts = dept.getSubDepartments();
            if (subDepts != null && !subDepts.isEmpty()) {
                sb.append("<ul class=\"hide\">"); // 하위부서 숨김
                for (Department childDept : subDepts) {
                    sb.append("<li>");
                    sb.append("<div class=\"group\" data-dept-id=\"").append(childDept.getDeptId()).append("\">")
                      .append(childDept.getDepartmentName()).append("</div>");

                    // 사원 목록 출력
                    List<EmployeeInfo> employees = childDept.getEmployees();
                    sb.append("<ul class=\"hide2\">");
                    if (employees != null && !employees.isEmpty()) {
                        for (EmployeeInfo emp : employees) {
                            sb.append("<li class=\"employee-item\" data-emp-id=\"").append(emp.getEmployeeId()).append("\">")
                              .append("<div class=\"profile\"><img src=\"").append(emp.getProfileUrl())
                              .append("\" alt=\"Profile\"></div>")
                              .append(emp.getName()).append("</li>");
                        }
                    }
                    sb.append("</ul>"); // hide2 끝
                    sb.append("</li>");
                }
                sb.append("</ul>"); // hide 끝
            }
            sb.append("</li>");
            return sb.toString();
        }
    %>
</body>
</html>
