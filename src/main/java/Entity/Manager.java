package Entity;

import Relation.Take;
import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Manager extends Employee {
    //部门主管查看本部门员工信息
    public static void getEmployeeMsg(Connection conn, String departmentID) {
        System.out.println("正在查找本部门所有的员工信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        String sql = SqlSentence.EMPLOYEE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        int num = executeSQL(conn, sql);
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }
    }

    //部门主管通过ID查看本部门员工信息
    public static void getEmployeeMsgByID(Connection conn, String departmentID, String employeeID) {
        System.out.println("正在查找本部门中ID为" + employeeID + "的员工信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        limits.put("employeeID", employeeID);
        String sql = SqlSentence.EMPLOYEE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        int num = executeSQL(conn, sql);
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }
    }

    //部门主管通过name查看本部门员工信息
    public static void getEmployeeMsgByName(Connection conn, String departmentID, String name) {
        System.out.println("正在查找本部门中姓名为" + name + "的员工信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        limits.put("employeeName", name);
        String sql = SqlSentence.EMPLOYEE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        int num = executeSQL(conn, sql);
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }
    }

    //部门主管查看本部门培训课程信息
    public static void getCourseMsg(Connection conn, String departmentID) {
        Course.getCourseMsg(conn, departmentID);

    }

    public static String getDepartmentIDByCourseID(Connection conn, String courseID) {
        return Course.getDepartmentIDByCourseID(conn, courseID);
    }

    //部门主管根据ID分配课程
    public static void assignCoursesByEmployeeID(Connection conn, String employeeID, String courseID) {
        Take.assignCoursesByEmployeeID(conn, employeeID, courseID);
    }

    //部门主管根据name分配课程
    public static void assignCoursesByEmployeeName(Connection conn, String employeeName, String courseID) {
        Take.assignCoursesByEmployeeName(conn, employeeName, courseID);
    }


}
