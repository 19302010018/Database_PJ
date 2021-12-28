package Relation;

import Entity.Employee;
import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class Take {
    public static void assignCoursesByEmployeeID(Connection conn, String employeeID, String courseID) {

        if (CourseBelong.checkCourseMandatory(conn, courseID)) {
            //是必修课
            System.out.println("这门课是必修课，不需要分配");
            return;
        }

        System.out.println("正在为员工" + employeeID + "分配课程" + courseID);

        LinkedHashMap msgs = new LinkedHashMap();
        msgs.put("employeeID", employeeID);
        msgs.put("courseID", courseID);
        msgs.put("finished", 0);
        String sql = SqlSentence.ASSIGN_COURSE + SqlSentence.insertClauseGenerator(msgs);

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void assignCoursesByEmployeeName(Connection conn, String employeeName, String courseID) {
        if (CourseBelong.checkCourseMandatory(conn, courseID)) {
            //是必修课
            System.out.println("这门课是必修课，不需要分配");
            return;
        }
        System.out.println("正在为员工" + employeeName + "分配课程" + courseID);

        String employeeID = Employee.getEmployeeIDByName(conn, employeeName);
        LinkedHashMap msgs = new LinkedHashMap();
        msgs.put("employeeID", employeeID);
        msgs.put("courseID", courseID);
        msgs.put("finished", 0);
        String sql = SqlSentence.ASSIGN_COURSE + SqlSentence.insertClauseGenerator(msgs);

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void autoAssignMandatoryCourse(Connection conn) {
        ArrayList<String> courses = new ArrayList<>();
        ArrayList<String> departments = new ArrayList<>();
        System.out.println("正在自动分配必修课程");
        String sql = SqlSentence.GET_MANDATORY_COURSES;
//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                courses.add(rs.getString("courseID").trim());
                departments.add(rs.getString("departmentID").trim());
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (int i = 0; i < courses.size(); i++) {
            String course = courses.get(i);
            String department = departments.get(i);
            ArrayList<String> employees = Employee.getEmployeeIDByDepartmentID(conn, department);
            for (int j = 0; j < employees.size(); j++) {
                String employee = employees.get(j);
                System.out.println("正在为员工" + employee + "分配课程" + course);

                LinkedHashMap msgs = new LinkedHashMap();
                msgs.put("employeeID", employee);
                msgs.put("courseID", course);
                msgs.put("finished", 0);
                String assign = SqlSentence.ASSIGN_COURSE + SqlSentence.insertClauseGenerator(msgs);

                try {
                    PreparedStatement ps = conn.prepareStatement(assign);
                    ps.execute();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }


        }

    }

    public static void registerGrades(Connection conn, String employeeID, String courseID, int grade) {
        HashMap limits = new HashMap();
        limits.put("employeeID", employeeID);
        limits.put("courseID", courseID);
        limits.put("finished", 0);

        System.out.println("正在查找" + employeeID + "在" + courseID + "课程中的考试信息");

        String sql = SqlSentence.GET_EXAM + SqlSentence.whereClauseGenerator(limits);

        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int num = 0;

            while (rs.next()) {
                num++;
            }
            if (num == 0) {
                System.out.println("这名员工曾上过的这门课都已经结课 无法登分");
            } else {
                System.out.println("可以登分");
                int status = 0;
                if (grade > 60) {
                    status = 1;
                }
                HashMap msgs = new HashMap();
                msgs.put("status", status);
                msgs.put("grade", grade);
                msgs.put("finished", 1);

                String register = SqlSentence.UPDATE_GRADE + SqlSentence.updateClauseGenerator(msgs) + SqlSentence.whereClauseGenerator(limits);

                PreparedStatement ps = conn.prepareStatement(register);
                ps.execute();

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}




