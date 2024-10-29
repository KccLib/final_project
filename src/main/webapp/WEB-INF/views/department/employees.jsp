<%@ page contentType="application/json; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo" %>

<%
     // 직원 정보 리스트를 요청 속성에서 가져옴
        List<EmployeeInfo> employees = (List<EmployeeInfo>) request.getAttribute("employees");
        response.setContentType("application/json"); // 응답 콘텐츠 타입 설정
        StringBuilder jsonBuilder = new StringBuilder(); // JSON 문자열 빌더
        jsonBuilder.append("[");

        // 직원 정보를 JSON 형식으로 변환
        for (int i = 0; i < employees.size(); i++) {
            EmployeeInfo emp = employees.get(i);
            jsonBuilder.append("{")
                .append("\"employeeId\":").append(emp.getEmployeeId()).append(",")
                .append("\"name\":\"").append(emp.getName()).append("\",")
                .append("\"position\":\"").append(emp.getPosition()).append("\",")
                .append("\"profileUrl\":\"").append(emp.getProfileUrl()).append("\"")
                .append("}");

            // 마지막 직원이 아닐 경우 쉼표 추가
            if (i < employees.size() - 1) {
                jsonBuilder.append(",");
            }
        }

        jsonBuilder.append("]"); // JSON 배열 종료
        out.print(jsonBuilder.toString()); // JSON 문자열 출력
%>
