package com.kcc.trioffice.domain.department.dto.response;

import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;

import java.util.List;

public class DepartmentUtils {

    public static String renderDept(Department dept) {
        StringBuilder sb = new StringBuilder();
        sb.append("<li class=\"menu\">");
        sb.append("<div class=\"menu2\" data-dept-id=\"").append(dept.getDeptId()).append("\" ")
                .append("data-dept-name=\"").append(dept.getDepartmentName()).append("\">")
                .append("<a>").append(dept.getDepartmentName()).append("</a>")
                .append("<div class=\"menu3\"><i class=\"fa-solid fa-ellipsis-vertical\"></i></div>")
                .append("</div>");

        List<Department> subDepts = dept.getSubDepartments();
        if (subDepts != null && !subDepts.isEmpty()) {
            sb.append("<ul class=\"hide\">");
            for (Department childDept : subDepts) {
                sb.append("<li>");
                sb.append("<div class=\"group\" data-dept-id=\"").append(childDept.getDeptId()).append("\" ")
                        .append("data-dept-name=\"").append(childDept.getDepartmentName()).append("\">")
                        .append("<a>").append(childDept.getDepartmentName()).append("</a>")
                        .append("<div class=\"ellipsis-icon2\"><i class=\"fa-solid fa-ellipsis-vertical\"></i></div>")
                        .append("</div>");

                List<EmployeeInfo> employees = childDept.getEmployees();
                sb.append("<ul class=\"hide2\">");
                if (employees != null && !employees.isEmpty()) {
                    for (EmployeeInfo emp : employees) {
                        sb.append("<li class=\"employee-item\" data-emp-id=\"").append(emp.getEmployeeId()).append("\">")
                                .append("<div class=\"profile\"><img src=\"").append(emp.getProfileUrl()).append("\" alt=\"Profile\" /></div>")
                                .append("<a>").append(emp.getName()).append("</a>")
                                .append("</li>");
                    }
                }
                sb.append("</ul>");
                sb.append("</li>");
            }
            sb.append("</ul>");
        }

        sb.append("</li>");
        return sb.toString();
    }
}
