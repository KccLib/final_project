package com.kcc.trioffice.domain.department.dto.response;

import com.kcc.trioffice.domain.employee.dto.response.EmployeeInfo;
import com.kcc.trioffice.domain.employee.dto.response.SearchEmployee;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TreeNode {

    private String id;
    private String parent;
    private String text;
    private String type;

    public static TreeNode of(DepartmentInfo departmentInfo) {

        if (departmentInfo.getUpperDeptId() == null) {
            return new TreeNode("dept_" + departmentInfo.getDeptId(), "#", departmentInfo.getName(), "department");
        }
        return new TreeNode("dept_" + departmentInfo.getDeptId(), "dept_" + departmentInfo.getUpperDeptId(), departmentInfo.getName(), "department");
    }

    public static TreeNode of(SearchEmployee searchEmployee) {
        String name = searchEmployee.getName() + " " + searchEmployee.getPosition();
        return new TreeNode(searchEmployee.getId().toString(), "dept_" + searchEmployee.getDeptId(), name, "employee");
    }
}
