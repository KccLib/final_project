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
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/department/adminDepartment.css" />
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" rel="stylesheet">
    <link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-square.css" rel="stylesheet" />
    <title>관리자 조직도</title>

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
                <div class="add-department-form" style="display: none;">
                    <p>부서 추가</p>
                    <div class="form-row">
                        <label for="upper-dept" style="margin-left: 50px; font-size: 20px;">상위부서</label>
                        <select id="upper-dept" class="form-control" style="margin-right: 160px; border: 1.5px solid black;" onchange="updateDepartmentName(this)">
                            <option value="">kcc정보통신</option>
                            <!-- 부서 목록을 동적으로 표시 -->
                            <c:forEach var="department" items="${departmentTree}">
                                <option value="${department.deptId}">${department.departmentName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <br>
                    <div class="form-row">
                        <label for="dept-name" style="margin-left: 48px;font-size: 20px;">*부서명 (한글)</label>
                        <input type="text" id="dept-name" class="form-control" style="margin-right: 80px;border: 1.5px solid black;" placeholder="부서명을 입력하세요">
                    </div>
                    <br>
                    <button id="save-dept" class="btn btn-primary" style="margin-top: 50px;margin-left: 180px;background-color: #0056b3;width: 90px;">저장</button>
                    <button id="cancel-dept" class="btn btn-secondary" style="margin-top: 50px;margin-left: 10px;width: 90px;">취소</button>
                </div>


            <div class="add-employee-form" style="display: block;">
                <p>사원 등록</p>

                <div class="form-row2" style="position: relative;">
                    <i class="fa-solid fa-circle-user"></i>
                    <i class="fa-regular fa-pen-to-square" style="position: absolute; font-size: 1.5em;left: 414px;top: 150px;"></i>
                </div>

                <br>

                <div class="form-row">
                    <label for="employee-name" style="margin-left: 130px; font-size: 20px;">이름(한글)</label>
                    <input type="text" id="employee-name" class="form-control" style="margin-right: 80px; border: 1.5px solid black;" placeholder="사원명을 입력하세요">
                </div>
                <br>
                <div class="form-row">
                    <label for="employee-id" style="margin-left: 130px; font-size: 20px;">*아이디</label>
                    <input type="text" id="employee-id" class="form-control" style="margin-right: 80px; border: 1.5px solid black;" placeholder="아이디를 입력하세요">
                </div>
                <br>
                <div class="form-row">
                    <label for="employee-password" style="margin-left: 130px; font-size: 20px;">*비밀번호</label>
                    <input type="text" id="employee-password" class="form-control" style="margin-right: 80px; border: 1.5px solid black;" placeholder="비밀번호를 입력하세요">
                </div>
                <br>
                <div class="form-row">
                    <label for="employee-password2" style="margin-left: 130px; font-size: 20px;">*비밀번호 확인</label>
                    <input type="text" id="employee-password2" class="form-control" style="margin-right: 80px; border: 1.5px solid black;" placeholder="비밀번호를 입력하세요">
                </div>
                <br>

                <div class="form-row" style="display: flex; align-items: center;">
                    <label for="position" style="margin-left: 130px; font-size: 20px;">직위</label>
                    <select id="position" class="form-control" style="margin-right: 160px; border: 1.5px solid black;" onchange="updatePositionName(this)">
                        <option value="">선택하세요</option>
                    </select>
                </div>

                <br>
                <div class="form-row" style="display: flex; align-items: center;">
                    <label for="upper-dept" style="margin-left: 130px; font-size: 20px;">부서</label>
                    <select id="upper-dept" class="form-control" style="margin-right: 160px; border: 1.5px solid black;" onchange="updatePositionName(this)">
                        <option value="">kcc정보통신</option>
                        <c:forEach var="department" items="${departmentTree}">
                            <option value="${department.deptId}">${department.departmentName}</option>
                        </c:forEach>
                    </select>
                </div>

                <button id="save-employee" class="btn btn-primary" style="margin-left: 250px;background-color: #0056b3;width: 90px;">저장</button>
                <button id="cancel-employee" class="btn btn-secondary" style="margin-left: 10px;width: 90px;">취소</button>
            </div>




                <!-- 부서 수정 폼 -->
                        <div class="edit-department-form" style="display: none;">
                            <h2>부서 수정</h2>
                            <form id="edit-department" action="/departments/updateDepartment" method="POST">
                                <div class="form-row">
                                    <label for="upper-dept">상위 부서 선택:</label>
                                    <select id="upper-dept" name="upperDeptId">
                                        <option value="">선택하세요</option>
                                        <c:forEach var="department" items="${upperDepartments}">
                                            <option value="${department.deptId}">${department.departmentName}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                                <div class="form-row">
                                    <label for="dept-name">부서명:</label>
                                    <input type="text" id="dept-name" name="departmentName" required>
                                </div>
                                <div class="form-buttons">
                                    <input type="hidden" id="dept-id" name="deptId">
                                    <button type="submit" id="save-dept">부서 수정</button>
                                    <button type="button" id="cancel-dept">취소</button>
                                </div>
                            </form>
                        </div>


                <!-- 부서 삭제 폼 -->
                <div id="cancel-department-form" style="display: none;">
                    <p>부서 삭제</p>
                    <div class="form-row">
                        <label for="selected-department-name" style="font-size: 20px;">삭제할 부서: </label>
                        <span id="selected-department-name" style="font-size: 20px; font-weight: bold;"></span>
                    </div>
                    <br>
                    <button id="confirm-delete" class="btn btn-danger" style="margin-top: 50px; margin-left: 180px; background-color: red; width: 90px;">삭제</button>
                    <button id="cancel-delete" class="btn btn-secondary" style="margin-top: 50px; margin-left: 10px; width: 90px;">취소</button>
                </div>

                <!-- 최종 삭제 확인 모달 -->
                <div id="delete-confirmation-modal" class="modal" style="display: none;">
                    <div class="modal-content">
                        <span class="close" style="cursor:pointer;">&times;</span>
                        <p>정말로 해당 부서를 삭제하시겠습니까?</p>
                        <button id="confirm-delete-department" class="btn btn-danger" style="margin-right: 10px;">삭제</button>
                        <button id="cancel-delete-department" class="btn btn-secondary">취소</button>
                    </div>
                </div>



            <div class="img2">
                <img src="/static/component/kcc-logo.png" style="
                                                              display: none;
                                                          ">
            </div>

        </div>
    </div>
</div>

<!-- 드롭다운 메뉴 구조 수정 -->
<div class="btn-group dropend2" id="dropdownMenu" style="display: none;">
    <ul class="dropdown-menu2"> <!-- 기본적으로 숨김 -->
        <li class="dropdown-item-container"><a class="dropdown-item add-department" href="#">부서 추가</a></li>
        <li class="dropdown-item-container"><a class="dropdown-item register" href="#">사원 등록</a></li>
        <li class="dropdown-item-container"><a class="dropdown-item modify" href="#">부서 수정</a></li>
        <li class="dropdown-item-container"><a class="dropdown-item delete" href="#">부서 삭제</a></li>
    </ul>
</div>

<script>
        $(document).ready(function() {
                // upper-dept 선택 시 부서명 출력
                $('#upper-dept').change(function() {
                    updateDepartmentName();  // 부서명 갱신 함수 호출
                });

                // 부서명 갱신 함수 정의
                function updateDepartmentName() {
                    var selectedDeptName = $('#upper-dept').find('option:selected').text();  // 선택된 부서명 가져오기

                    // 부서명이 빈 값이 아닌 경우에만 표시
                    if (selectedDeptName) {
                        $('#selected-department-name').text(selectedDeptName);  // 부서명 출력할 위치
                    }
                }

            // 상위부서를 클릭했을 때 하위부서가 표시됨
            $(document).on('click', '.menu2 > a', function () {
                var $subDepartments = $(this).parent().siblings('ul.hide');
                if ($subDepartments.length > 0) {
                    $subDepartments.slideToggle();
                    $(this).parent().toggleClass('selected');
                }
            });

            $('#dropdownMenu .dropdown-toggle').on('click', function(e) {
                e.preventDefault();
                e.stopPropagation(); // 클릭 이벤트 전파 방지
                var $dropdownMenu = $(this).next('.dropdown-menu');

                $dropdownMenu.toggle(); // 드롭다운 메뉴 토글
            });

            // 문서의 다른 부분 클릭 시 드롭다운 숨김
            $(document).on('click', function() {
                $('#dropdownMenu .dropdown-menu').hide(); // 드롭다운 메뉴 숨김
            });

            // 하위부서를 클릭했을 때 사원 목록이 표시됨
            $(document).on('click', '.group', function (event) {
                event.stopPropagation();
                var $employees = $(this).siblings('ul.hide2');
                var deptId = $(this).data('dept-id');
                var subDeptName = $(this).text();
                var topDeptName = $(this).closest('.menu').find('.menu2 a').text();

                if ($employees.length > 0) {
                    if (!$employees.hasClass('loaded')) {
                        $.ajax({
                            url: "/departments/" + deptId + "/employees",
                            type: "GET",
                            success: function (data) {
                                var employeeList = "";
                                $.each(data, function (index, employee) {
                                    employeeList += "<li class='employee-item' data-emp-id='" + employee.employeeId + "'>" +
                                        "<div class='profile'>" +
                                        "<img src='" + employee.profileUrl + "' alt='Profile' />" +
                                        "</div>" +
                                        "<a>" + employee.name + "</a>" +
                                        "<div class='menu4'>" +
                                        "<i class='fa-solid fa-ellipsis-vertical'></i>" +
                                        "</div>" +
                                        "</li>";
                                });

                                if (employeeList === "") {
                                    employeeList = "<li>사원이 없습니다</li>";
                                }

                                $employees.html(employeeList);
                                $employees.addClass('loaded');
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.error("사원 정보를 불러오는데 실패했습니다.", textStatus, errorThrown);
                                alert("사원 정보를 불러오는데 실패했습니다.");
                            }
                        });
                    }

                    $employees.slideToggle();
                }

                $(this).toggleClass('selected');
                $('.top-department').text(topDeptName);
                $('.sub-department').text(subDeptName);
            });

            // 직원 항목 클릭 시 정보 표시
            $(document).on('click', '.employee-item', function() {
                $('.contents-1-1').show();
                $('.dept-sub').show();
                $('.contents-1').show();
                $('.contents-2').show();

                var employeeId = $(this).data('emp-id');
                $.ajax({
                    url: "/api/employees/" + employeeId,
                    type: "GET",
                    success: function(data) {
                        $('.contents-1 .employee-name').text(data.name);
                        $('.contents-1 .dept-emp').text(data.position);
                        $('.contents-1 .email').text(data.email);
                        $('.contents-1 .profile2 img').attr('src', data.profileUrl);
                        $('.contents-2 .info-values p:eq(0)').text(data.companyName);
                        $('.contents-2 .info-values p:eq(1)').text(data.deptName);
                        $('.contents-2 .info-values p:eq(2)').text(data.subDeptName);
                        $('.contents-2 .info-values p:eq(3)').text(data.position);
                        $('.contents-2 .info-values p:eq(4)').text(data.phoneNum);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error("직원 정보를 불러오는데 실패했습니다.", textStatus, errorThrown);
                        alert("직원 정보를 불러오는데 실패했습니다.");
                    }
                });
            });

            // menu3 클릭 시 드롭다운 표시
            $(document).on('click', '.menu3', function(e) {
                e.stopPropagation(); // 클릭 이벤트 전파 방지
                var $fixedMenu = $(this).closest('.second-side-bar');
                var dropdown = $('#dropdownMenu');

                // 드롭다운 표시 및 위치 조정
                dropdown.toggle().css({
                    top: $(this).offset().top - 960 + 'px', // 메뉴 아래
                    left: $fixedMenu.offset().left + $fixedMenu.outerWidth() - 2 + 'px', // fixed-menu 오른쪽으로 정렬
                    backgroundColor: '#f5f5f5', // 배경 색상 설정
                    border: '1.4px solid rgb(0 0 0)' // 경계선 설정
                });

                // 내부 li 항목에 스타일 적용
                $('.dropdown-menu2 li').css({
                    borderRadius: '8px', // 둥근 모서리
                    padding: '5px 5px', // 여백 추가
                    transition: 'background-color 0.3s', // 부드러운 배경색 전환
                    backgroundColor: '#ffffff', // 기본 배경 색상 설정 (흰색)
                    marginTop: '3px', // 위쪽 여백 설정
                    marginBottom: '3px' // 아래쪽 여백 설정
                });

                // 마우스를 올렸을 때의 배경색 변경
                $('.dropdown-item-container').hover(
                    function() {
                        $(this).find('a').css('background-color', 'rgba(0, 123, 255, 0.5)'); // li 안의 a 태그 배경색 변경
                    },
                    function() {
                        $(this).css('background-color', '#ffffff'); // 마우스를 떼었을 때 기본 색상으로 되돌리기
                        $(this).find('a').css('background-color', '#ffffff'); // li 안의 a 태그 배경색 기본으로 되돌리기
                    }
                );

                // a 태그에 스타일 적용
                $('.dropdown-menu2 li a').css({
                    fontWeight: 'bold', // 폰트 두께 설정
                    textDecoration: 'none' // 기본 밑줄 제거 (원하는 경우)
                });

                // 메뉴 항목들을 바로 표시
                dropdown.find('.dropdown-menu').show();
            });

            // 문서의 다른 부분 클릭 시 드롭다운 숨김
            $(document).on('click', function() {
                $('#dropdownMenu').hide();
            });

            // 부서 추가 버튼 클릭 시 부서 추가 폼 표시
            $(document).on('click', '.add-department', function(e) {
                e.preventDefault();
                $('.add-department-form').show();  // 부서 추가 폼 보이기
                $('.img2').hide();
                $('.add-employee-form').hide();
                $('.edit-department-form').hide();
                $('#cancel-department-form').hide();
            });


            $(document).on('click', '#save-dept', function(e) {
                e.preventDefault();  // 기본 form 제출 방지
                var upperDept = $('#upper-dept').val();  // 선택한 상위부서
                var deptName = $('#dept-name').val();    // 입력한 부서명

                if (deptName === "") {
                    alert("부서명을 입력하세요.");
                    return;
                }

                // 부서 저장 AJAX 로직
                $.ajax({
                    url: "/departments/saveDepartment",  // 경로 수정
                    type: "POST",
                    data: {
                        upperDeptId: upperDept,
                        departmentName: deptName
                    },
                    success: function(response) {
                        alert("부서가 성공적으로 추가되었습니다.");
                        $('.add-department-form').hide();  // 폼 숨기기
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error("부서 추가에 실패했습니다.", textStatus, errorThrown);
                        alert("부서 추가에 실패했습니다.");
                    }
                });
            });

            // 취소 버튼 클릭 시 폼 숨기기 및 초기화
            $(document).on('click', '#cancel-dept', function(e) {
                e.preventDefault();
                $('.add-department-form').hide();  // 폼 숨기기
                $('#dept-name').val('');  // 입력된 내용 초기화
            });


            // 사원 등록 버튼 클릭 시 사원 등록 폼 표시
                $(document).on('click', '.register', function(e) {
                    e.preventDefault();
                    $('.add-employee-form').show();  // 사원 등록 폼 보이기
                    $('.img2').hide();  // img2 요소 숨기기 (필요 시)
                    $('.add-department-form').hide();
                    $('#cancel-department-form').hide();
                    $('.edit-department-form').hide();
                });

                // 저장 버튼 클릭 시 처리 (추후 백엔드로 데이터 전송 등 구현 가능)
                $(document).on('click', '#save-employee', function(e) {
                    e.preventDefault();  // 기본 form 제출 방지
                    var employeeName = $('#employee-name').val();    // 입력한 사원명
                    var employeeEmail = $('#employee-email').val();  // 입력한 이메일

                    if (employeeName === "" || employeeEmail === "") {
                        alert("모든 필드를 입력하세요.");
                    } else {
                        // 데이터 저장을 위한 처리 (AJAX 요청 또는 form 제출 로직 추가 가능)
                        alert("사원이 등록되었습니다.");
                        $('.add-employee-form').hide();  // 사원 등록 폼 숨기기
                    }
                });

                // 취소 버튼 클릭 시 폼 숨김
                $(document).on('click', '#cancel-employee', function(e) {
                    e.preventDefault();
                    $('.add-employee-form').hide();  // 사원 등록 폼 숨기기
                    $('.img2').show();  // img2 요소 다시 보이기 (필요 시)
                });


                // 부서 삭제
                $(document).on('click', '.delete', function(e) {
                    e.preventDefault();

                    // 클릭한 부서의 정보를 가져오는 부분
                    var departmentName = $(this).closest('.department-item').data('department-name');
                    var departmentId = $(this).closest('.department-item').data('department-id');

                    console.log(departmentName, departmentId); // 디버깅을 위한 로그

                    // 선택한 부서 이름을 삭제 폼에 표시
                    $('#selected-department-name').text(departmentName).data('department-id', departmentId);

                    // 삭제 폼을 표시
                    $('#cancel-department-form').show();
                    $('.add-employee-form').hide();
                    $('.add-department-form').hide();
                    $('.edit-department-form').hide();
                    $('.img2').hide();
                });

                // 삭제 버튼 클릭 시 최종 삭제 확인 모달 열기
                $(document).on('click', '#confirm-delete', function(e) {
                    e.preventDefault();
                    $('#cancel-department-form').hide(); // 기존 삭제 폼 숨기기
                    $('#delete-confirmation-modal').show(); // 모달 열기
                });




                    // 부서 수정 버튼 클릭 시 사원 등록 폼 표시
                                    $(document).on('click', '.modify', function(e) {
                                        e.preventDefault();
                                        $('.edit-department-form').show();  // 사원 등록 폼 보이기
                                        $('.img2').hide();  // img2 요소 숨기기 (필요 시)
                                        $('.add-department-form').hide();
                                        $('#cancel-department-form').hide();
                                        $('.add-employee-form').hide();
                                    });
                                    add-employee-form



        });
    </script>



        <%!
            public String renderDept(Department dept) {
                StringBuilder sb = new StringBuilder();
                sb.append("<li class=\"menu\">");
                sb.append("<div class=\"menu2\">")
                  .append("<a>").append(dept.getDepartmentName()).append("</a>")
                  .append("<div class=\"menu3\"><i class=\"fa-solid fa-ellipsis-vertical\"></i></div>")
                  .append("</div>"); // menu2 끝

                // 하위 부서가 있는 경우만 출력
                List<Department> subDepts = dept.getSubDepartments();
                if (subDepts != null && !subDepts.isEmpty()) {
                    sb.append("<ul class=\"hide\">"); // 하위부서 숨김
                    for (Department childDept : subDepts) {
                        sb.append("<li>");
                        sb.append("<div class=\"group\" data-dept-id=\"").append(childDept.getDeptId()).append("\">")
                          .append("<a>").append(childDept.getDepartmentName()).append("</a>")
                          .append("<div class=\"ellipsis-icon2\"><i class=\"fa-solid fa-ellipsis-vertical\"></i></div>") // ellipsis-icon2 추가
                          .append("</div>"); // group 끝

                        // 사원 목록 출력
                        List<EmployeeInfo> employees = childDept.getEmployees();
                        sb.append("<ul class=\"hide2\">"); // 사원 목록 숨김
                        if (employees != null && !employees.isEmpty()) {
                            for (EmployeeInfo emp : employees) {
                                sb.append("<li class=\"employee-item\" data-emp-id=\"").append(emp.getEmployeeId()).append("\">")
                                  .append("<div class=\"profile\"><img src=\"").append(emp.getProfileUrl()).append("\" alt=\"Profile\" /></div>")
                                  .append("<a>").append(emp.getName()).append("</a>")
                                  .append("</li>");
                            }
                        }
                        sb.append("</ul>"); // hide2 끝
                        sb.append("</li>");
                    }
                    sb.append("</ul>"); // hide 끝
                }

                sb.append("</li>"); // menu 끝
                return sb.toString();
            }
        %>


</body>
</html>
