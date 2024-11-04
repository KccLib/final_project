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
                        out.print("<li class='menu' data-dept-id='" + dept.getDeptId() + "' data-dept-name='" + dept.getDepartmentName() + "'>");
                        out.print(renderDept(dept)); // 최상위 부서만 출력
                        out.print("</li>");
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
                        <label for="upper-dept2" style="margin-left: 50px; font-size: 20px;">상위부서</label>
                        <select id="upper-dept2" class="form-control" style="margin-right: 160px; border: 1.5px solid black;"
                        onchange="updateDepartmentName2(this)">
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


            <div class="add-employee-form" style="display: none;">
                <p>사원 등록</p>

                <div class="form-row2" style="position: relative;width: 160px;margin-left: 257px;">
                    <i class="fa-solid fa-circle-user" style="margin-left: 0px;"></i>
                    <i class="fa-regular fa-pen-to-square" style="position: absolute;font-size: 1.5em;left: 134px;top: 116px;"></i>
                </div>
                <br>

                <div class="form-row" style="margin-top: 15px;">
                                    <label for="employee-name" style="margin-left: 70px;font-size: 20px;border-top-width: 3px;padding-top: 8px;">이름(한글)</label>
                                    <input type="text" id="employee-name" class="form-control" style="margin-right: 80px;border: 1.5px solid black;width: 235.666666px;" placeholder="사원명을 입력하세요">
                                </div>
                <div class="form-row" style="margin-top: 9px;">
                                    <label for="employee-id" style="margin-left: 70px;font-size: 20px;padding-top: 8px;">*아이디</label>
                                    <input type="text" id="employee-id" class="form-control" style="margin-right: 80px;border: 1.5px solid black;margin-left: 120px;width: 235.666666px;" placeholder="아이디를 입력하세요">  @kcc.co.kr
                                </div>
                <div class="form-row" style="margin-top: 9px;">
                                    <label for="employee-password" style="margin-left: 70px;font-size: 20px;padding-top: 8px;">*비밀번호</label>
                                    <input type="text" id="employee-password2" class="form-control" style="margin-right: 80px;border: 1.5px solid black;margin-left: 102px;width: 235.666666px;" placeholder="비밀번호를 입력하세요">
                                </div>
                <div class="form-row" style="margin-top: 9px;">
                                    <label for="employee-password2" style="margin-left: 70px;font-size: 20px;padding-top: 8px;">*비밀번호 확인</label>
                                    <input type="text" id="employee-password2" class="form-control" style="margin-right: 80px;border: 1.5px solid black;margin-left: 62px;width: 235.666666px;" placeholder="비밀번호를 입력하세요">
                                </div>

                <div class="form-row" style="display: flex;align-items: center;margin-top: 9px;">
                                    <label for="position" style="margin-left: 70px;font-size: 20px;">직위</label>
                                    <select id="position" class="form-control" style="margin-right: 160px;border: 1.5px solid black;margin-left: 145px;width: 235.666666px;" onchange="updatePositionName(this)">
                                        <option value="">선택하세요</option>
                                        <option value="사원">사원</option>
                                        <option value="대리">대리</option>
                                        <option value="과장">과장</option>
                                        <option value="차장">차장</option>
                                        <option value="부장">부장</option>
                                        <option value="이사">이사</option>
                                        <option value="상무">상무</option>
                                        <option value="전무">전무</option>
                                        <option value="대표이사">대표이사</option>
                                    </select>
                                </div>

                <div class="form-row" style="display: flex;align-items: center;margin-top: 9px;">
                    <label for="upper-dept" style="margin-left: 70px; font-size: 20px;">부서</label>
                    <select id="upper-dept" class="form-control" style="margin-left: 145px;margin-right: 160px; border: 1.5px solid black;width: 235.666666px;" onchange="updatePositionName(this)">
                        <option value="">kcc정보통신</option>
                        <c:forEach var="department" items="${departmentTree}">
                            <option value="${department.deptId}">${department.departmentName}</option>
                        </c:forEach>
                    </select>
                </div>

                <button id="save-employee" class="btn btn-primary" style="margin-left: 250px;background-color: #0056b3;width: 90px;margin-top: 25px;">저장</button>
                <button id="cancel-employee" class="btn btn-secondary" style="margin-left: 10px;width: 90px;margin-top: 25px;">취소</button>
            </div>



            <div class="edit-department-form" style="display: none; height: 422px;">
                <p style="margin-bottom: 55px;">부서 수정</p>

                <div class="form-row" style="margin-bottom: 15px; font-size: 20px;">
                    <label class="label-title" style="color: #333; font-weight: bold; width: 150px; margin-left: 50px;">부서명 (한글)</label>
                    <div class="label-content" data-editable="false" style="color: #555; background-color: #f0f0f0; padding: 5px 10px; border-radius: 5px;">부서명</div>
                    <i class="fa-solid fa-pen" style="margin-left: 10px; cursor: pointer;" onclick="enableEdit(this)"></i>
                </div>

                <div class="form-row" style="margin-bottom: 15px; font-size: 20px;">
                    <label class="label-title" style="color: #333; font-weight: bold; width: 150px; margin-left: 50px;">상위 부서</label>

                    <div class="label-content" data-editable="false" style="color: #555; background-color: #f0f0f0; padding: 5px 10px; border-radius: 5px;">
                        선택된 부서명
                    </div>

                    <!-- 상위 부서 선택 드롭다운 -->
                    <select class="form-control top-department-dropdown" style="display: none;width: 160px;" onchange="updateSelectedDepartment(this)">
                        <option value="">상위 부서 선택</option>
                        <c:forEach var="department" items="${departmentTree}">
                            <c:if test="${department.upperDeptId == null}">
                                <option value="${department.deptId}">${department.departmentName}</option>
                            </c:if>
                        </c:forEach>
                    </select>

                    <!-- 수정 아이콘 -->
                    <i class="fa-solid fa-pen" style="margin-left: 10px; cursor: pointer;" onclick="toggleDepartmentDropdown(this)"></i>
                </div>

                <div class="form-row" style="margin-bottom: 15px; font-size: 20px;">
                    <label class="label-title" style="color: #333; font-weight: bold; width: 150px; margin-left: 50px;">하위 부서</label>
                    <div class="label-content" data-editable="false" style="color: #555; background-color: #f0f0f0; padding: 5px 10px; border-radius: 5px;">하위 부서명</div>
                </div>

                <div class="form-row" style="margin-bottom: 15px; font-size: 20px;">
                    <label class="label-title" style="color: #333; font-weight: bold; width: 150px; margin-left: 50px;">생성일</label>
                    <div class="label-content" style="color: #555; background-color: #f0f0f0; padding: 5px 10px; border-radius: 5px;">YYYY-MM-DD</div>
                </div>

                <div class="button-row" style="display: flex; justify-content: center; gap: 10px; margin-top: 20px;">
                    <button class="save-button" style="background-color: #0056b3; color: #fff; padding: 8px 20px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; margin-top: 40px; width: 104px; height: 40px;">저장</button>
                    <button class="cancel-button" style="background-color: #6c757d; color: #fff; padding: 8px 20px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; margin-top: 40px; width: 104px;">취소</button>
                </div>
            </div>



            <br>
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
                <img src="/static/component/kcc-logo.png">
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
    <input type="hidden" id="deptId" value=""/>
</div>

<script>
        // 드롭다운 표시/숨김 전환 함수
        function toggleDepartmentDropdown(icon) {
            const labelContent = icon.previousElementSibling.previousElementSibling;
            const dropdown = icon.previousElementSibling;

            if (dropdown.style.display === "none" || dropdown.style.display === "") {
                labelContent.style.display = "none";
                dropdown.style.display = "block";
            } else {
                labelContent.style.display = "block";
                dropdown.style.display = "none";
            }
        }

        let selectedOptionValue = null; // 전역 변수
        let updatedDepartmentName = null;

        // 드롭다운 선택 시 부서 ID 업데이트
        function updateSelectedDepartment(selectElement) {
            const selectedOptionText = selectElement.options[selectElement.selectedIndex].text; // 선택된 부서명
            selectedOptionValue = selectElement.options[selectElement.selectedIndex].value; // 선택된 부서 ID를 전역 변수에 할당
            const labelContent = selectElement.previousElementSibling;

            // 선택된 부서명 표시
            labelContent.innerText = selectedOptionText;
            console.log("선택된 부서 ID:", selectedOptionValue); // 부서 ID 출력

            // 드롭다운 숨기기 및 label-content 표시
            selectElement.style.display = "none";
            labelContent.style.display = "block";
        }

        // 전역 변수로 upperDept 값을 저장
        let selectedUpperDept = null;

        function enableEdit(icon) {
            const labelContent = icon.previousElementSibling;

            if (labelContent && labelContent.dataset.editable === "false") {
                const text = labelContent.innerText;
                const input = document.createElement("input");

                input.type = "text";
                input.value = text;
                input.style.padding = "5px 10px";
                input.style.borderRadius = "5px";
                input.style.border = "1px solid #ccc";

                labelContent.innerHTML = "";
                labelContent.appendChild(input);
                labelContent.dataset.editable = "true";

                input.focus();

                // Input 필드 벗어날 때 또는 다른 아이콘을 클릭할 때 수정된 값을 저장
                input.addEventListener("blur", function () {
                    updatedDepartmentName = input.value; // 전역 변수에 수정된 값 저장
                    labelContent.innerText = updatedDepartmentName; // 화면에 표시된 텍스트 업데이트
                    labelContent.dataset.editable = "false";
                    console.log("수정된 부서 이름:", updatedDepartmentName); // 콘솔에 출력
                });

                // 다른 아이콘 클릭 시에도 수정된 값 저장
                icon.addEventListener("click", function () {
                    if (labelContent.dataset.editable === "true") {
                        updatedDepartmentName = input.value; // 전역 변수에 수정된 값 저장
                        labelContent.innerText = updatedDepartmentName; // 화면에 표시된 텍스트 업데이트
                        labelContent.dataset.editable = "false";
                        console.log("수정된 부서 이름:", updatedDepartmentName); // 콘솔에 출력
                    }
                });
            }
        }


        // 상위 부서 선택 시 호출되는 함수
        function updateDepartmentName2(element) {
            selectedUpperDept = element.value;  // 선택된 값을 전역 변수에 저장
            console.log("Selected value:", selectedUpperDept);
        }

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

            // 문서의 다른 부분을 클릭했을 때 드롭다운을 숨기기
            $(document).on('click', function(e) {
                var $dropdown = $('#dropdownMenu');

                // 클릭한 요소가 드롭다운이나 menu3 또는 ellipsis-icon2가 아닌 경우에만 드롭다운 숨김
                if (!$dropdown.is(e.target) && $dropdown.has(e.target).length === 0 &&
                    !$(e.target).closest('.menu3').length && !$(e.target).closest('.ellipsis-icon2').length) {
                    $dropdown.hide();
                }
            });

            // 하위부서를 클릭했을 때 사원 목록이 표시됨
            $(document).on('click', '.group', function (event) {
                event.stopPropagation(); // 상위 부서 이벤트 전파 방지

                var $employees = $(this).siblings('ul.hide2'); // 'group' 요소의 형제 요소 중 'ul.hide2' 찾기
                var deptId = $(this).data('dept-id'); // 클릭한 부서의 deptId를 가져옴
                var subDeptName = $(this).text(); // 클릭한 하위 부서의 이름 가져오기
                var topDeptName = $(this).closest('.menu').find('.menu2 a').text(); // 최상위 부서 이름 가져오기

                if ($employees.length > 0) {
                    if (!$employees.hasClass('loaded')) {
                        // 아직 사원 정보가 로드되지 않았을 때만 AJAX 호출
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
                                $employees.slideDown(); // 부드럽게 사원 목록 표시
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.error("사원 정보를 불러오는데 실패했습니다.", textStatus, errorThrown);
                                alert("사원 정보를 불러오는데 실패했습니다.");
                            }
                        });
                    } else {
                        // 이미 로드된 경우에는 토글로 표시
                        $employees.slideToggle();
                    }
                }

                $(this).toggleClass('selected'); // 선택된 부서 표시

                // 부서 이름 업데이트
                $('.top-department').text(topDeptName); // 최상위 부서 이름 설정
                $('.sub-department').text(subDeptName); // 하위 부서 이름 설정
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
            $(document).on('click', '.menu3, .ellipsis-icon2', function(e) {
                console.log('Clicked element:', this); // 클릭된 요소 출력
                e.stopPropagation(); // 클릭 이벤트 전파 방지
                var $fixedMenu = $(this).closest('.second-side-bar');
                var dropdown = $('#dropdownMenu');

                // 클릭한 요소가 menu3인지 ellipsis-icon2인지에 따라 부모 요소 선택
                const selectedMenu = $(this).hasClass('menu3')
                    ? $(this).closest('.menu2') // menu3 클릭 시 부모 .menu2 선택
                    : $(this).closest('.group'); // ellipsis-icon2 클릭 시 부모 .group 선택

                console.log('선택한 메뉴:', selectedMenu); // 선택한 menu 또는 group 요소 출력하여 확인

                // 부서 ID 가져오기
                selectedDeptId = selectedMenu.data('dept-id'); // 부서 ID 저장
                console.log('부서 ID:', selectedDeptId); // 부서 ID 출력
                $("#deptId").val(selectedDeptId);
                console.log($("#deptId").val());

                // 부서 이름 및 기타 정보 가져오기
                selectedDeptName = selectedMenu.data('dept-name'); // 부서 이름 저장
                selectedUpperDeptName = selectedMenu.data('upperDeptId'); // 상위 부서명 저장

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

                // a 태그에 스타일 적용
                $('.dropdown-menu2 li a').css({
                    fontWeight: 'bold', // 폰트 두께 설정
                    textDecoration: 'none' // 기본 밑줄 제거 (원하는 경우)
                });
            });



            // 드롭다운 메뉴 항목 클릭 이벤트 처리
            $(document).on('click', '.dropdown-item', function(e) {
                e.preventDefault(); // 기본 클릭 동작 방지

                // 선택된 부서 ID를 사용하여 원하는 작업 수행
                console.log("내가 누른 부서 ID:", selectedDeptId); // 부서 ID 출력
                const action = $(this).text(); // 클릭한 항목의 텍스트 가져오기
                console.log("선택한 작업:", action); // 선택한 작업 출력
                $('#dropdownMenu').hide();

                // 여기에 부서 추가, 수정, 등록, 삭제 로직 추가
            });

            // 부서 추가 버튼 클릭 시 폼 표시
                    $(document).on('click', '.add-department', function(e) {
                        e.preventDefault();
                        $('.add-department-form').show();
                        $('.img2, .add-employee-form, .edit-department-form, #cancel-department-form').hide();
                    });

                    // 저장 버튼 클릭 시 AJAX 처리
                    $(document).on('click', '#save-dept', function(e) {
                        e.preventDefault();
                        $dropdown.hide();
                        var upperDept = selectedUpperDept;  // 전역 변수에서 값 가져오기
                        console.log("upperDept: " + upperDept);
                        if (upperDept == null || "") {
                            upperDept = 0;
                        }
                        var deptName = $('#dept-name').val();
                        console.log("deptName: " + deptName);

                        if (deptName.trim() === "") {
                            alert("부서명을 입력하세요.");
                            return;
                        }

                        $.ajax({
                            url: "/api/departments/save",
                            type: "POST",
                            data: {
                                upperDeptId: upperDept,  // upperDept가 null일 경우 null로 설정
                                departmentName: deptName
                            },
                            success: function(response) {
                                alert("부서가 성공적으로 추가되었습니다.");
                                $('.add-department-form').hide();
                                $('#department-list').append('<li>' + deptName + (upperDept ? ' (상위부서: ' + upperDept + ')' : '') + '</li>');
                                location.href = data; // 성공하면 페이지 리로드 또는 리다이렉트
                            },
                            error: function(jqXHR, textStatus, errorThrown) {
                                console.error("부서 추가에 실패했습니다.", textStatus, errorThrown);
                                alert("부서 추가에 실패했습니다. 오류: " + errorThrown);
                            }
                        });

                    });

                    // 취소 버튼 클릭 시 폼 숨기기 및 초기화
                    $(document).on('click', '#cancel-dept', function(e) {
                        e.preventDefault();
                        $('.add-department-form').hide();
                        $('#dept-name').val('');
                        selectedUpperDept = null;  // 선택된 상위 부서 값 초기화
                    });


            // 부서 수정
            $(document).on('click', '.dropdown-item.modify', function(e) {
                e.preventDefault(); // 기본 클릭 동작 방지
                let deptId = $("#deptId").val();

                // AJAX 요청으로 부서 정보 가져오기
                $.ajax({
                    url: '/api/departments/' + deptId, // 부서 정보를 가져올 API 엔드포인트
                    type: 'GET',
                    success: function(data) {
                        console.log("내가찍은data:", data);

                        if (data) {
                            // 부서 수정 폼에 부서 정보 출력
                            $('.edit-department-form .label-content').eq(0).text(data.departmentName); // 부서명 설정
                            $('.edit-department-form .label-content').eq(1).text(data.upperDptName || '없음'); // 상위 부서 설정

                            // 생성일 출력
                            let writeDt = data.writeDt; // writeDt를 변수에 저장
                            if (typeof writeDt === 'string') {
                                writeDt = writeDt.substring(0, 16); // '2024-10-21 15:17' 형식으로 변경
                            } else if (writeDt instanceof Date) {
                                const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', hour12: false };
                                writeDt = writeDt.toLocaleString('sv-SE', options).replace('T', ' '); // '2024-10-21 15:17' 형식으로 변경
                            }
                            $('.edit-department-form .label-content').eq(3).text(writeDt); // 생성일 설정


                            // 하위 부서 정보를 AJAX로 가져오기
                                        $.ajax({
                                            url: '/api/departments/' + deptId + '/sub', // 하위 부서 정보를 가져올 API 엔드포인트
                                            type: 'GET',
                                            success: function(subDepartments) {
                                                // 하위 부서 설정
                                                const subDeptNames = subDepartments.map(subDept => subDept.departmentName);
                                                $('.edit-department-form .label-content').eq(2).text(subDeptNames.join(", ") || '없음'); // 하위 부서 설정
                                            },
                                            error: function(jqXHR, textStatus, errorThrown) {
                                                console.error("하위 부서 정보 불러오기 실패:", textStatus, errorThrown);
                                                $('.edit-department-form .label-content').eq(2).text('하위 부서를 불러오는 데 실패했습니다.');
                                            }
                                        });

                            // 수정할 부서 정보를 가진 폼을 보이게 설정
                            $('.edit-department-form').show(); // 부서 수정 폼 보이기
                        } else {
                            alert("부서 정보를 불러오는 데 실패했습니다.");
                        }

                        // 관련 폼 및 요소 숨기기
                        $('.img2').hide();  // 필요 시 img2 요소 숨기기
                        $('.add-department-form').hide(); // 부서 추가 폼 숨기기
                        $('#cancel-department-form').hide(); // 취소 버튼 숨기기
                        $('.add-employee-form').hide(); // 사원 등록 폼 숨기기
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error("부서 정보 불러오기 실패:", textStatus, errorThrown);
                        alert("부서 정보를 불러오는 데 실패했습니다.");
                    }
                });
            });


            // 저장 버튼 클릭 이벤트
            $('.save-button').on('click', function(e) {
                e.preventDefault(); // 기본 클릭 동작 방지

                console.log("내가 저장할 버튼 id야", selectedDeptId);
                console.log("잘들어오나?:", updatedDepartmentName); // 전역 변수 출력


                // AJAX 요청으로 부서 수정
                $.ajax({
                    url: '/api/update/' + selectedDeptId, // 부서 수정 API 엔드포인트
                    type: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        deptId: selectedDeptId,
                        departmentName: updatedDepartmentName,
                        upperDeptId: selectedOptionValue
                    }),
                    success: function(response) {
                        alert("부서가 성공적으로 수정되었습니다.");
                        // 수정 후 필요에 따라 UI 업데이트
                        $('.edit-department-form').hide(); // 수정 폼 숨기기
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error("부서 수정 실패:", textStatus, errorThrown);
                        alert("부서 수정에 실패했습니다.");
                    }
                });
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


            $(document).on('click', '.delete', function(e) {
                e.preventDefault();
                console.log("선택한 부서 이름:", selectedDeptName);
                console.log("id!!!" + selectedDeptId);

                // 확인 후 삭제 진행
                if (confirm(selectedDeptName + " 부서를 정말 삭제하시겠습니까?")) {
                    $.ajax({
                        url: "/api/delete/" + selectedDeptId,
                        type: "DELETE",
                        success: function(response) {
                            alert(selectedDeptName + " 부서가 삭제되었습니다.");
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            console.error("부서 삭제에 실패했습니다.", textStatus, errorThrown);
                            alert("부서 삭제에 실패했습니다.");
                        }
                    });
                }
            });


        });
    </script>

<%!
public String renderDept(Department dept) {
System.out.println("부서 ID: " + dept.getDeptId());
    StringBuilder sb = new StringBuilder();
    sb.append("<li class=\"menu\">");
    sb.append("<div class=\"menu2\" data-dept-id=\"").append(dept.getDeptId()).append("\" ")
      .append("data-dept-name=\"").append(dept.getDepartmentName()).append("\">")
      .append("<a>").append(dept.getDepartmentName()).append("</a>")
      .append("<div class=\"menu3\"><i class=\"fa-solid fa-ellipsis-vertical\"></i></div>")
      .append("</div>"); // menu2 끝

    // 하위 부서가 있는 경우만 출력
    List<Department> subDepts = dept.getSubDepartments();
    if (subDepts != null && !subDepts.isEmpty()) {
        sb.append("<ul class=\"hide\">"); // 하위부서 숨김
        for (Department childDept : subDepts) {
            sb.append("<li>");
            sb.append("<div class=\"group\" data-dept-id=\"").append(childDept.getDeptId()).append("\" ")
              .append("data-dept-name=\"").append(childDept.getDepartmentName()).append("\">")
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
