package Entity;

import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Course {
    //部门主管查看本部门培训课程信息
    public static void getCourseMsg(Connection conn, String departmentID) {
        System.out.println("正在查找本部门所有的培训课程信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        String sql = SqlSentence.COURSE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        int num = executeSQL(conn, sql);
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }


    }

    public static int executeSQL(Connection conn, String sql) {
        int num = 0;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                num++;
                String courseID = rs.getString("courseID").trim();
                String name = rs.getString("name").trim();
                String type = rs.getString("type").trim();
                String syllabus = rs.getString("syllabus").trim();


                System.out.println(courseID + "  |  " +
                        name + "  |  " +
                        type + "  |  " +
                        syllabus
                );
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    public static String getDepartmentIDByCourseID(Connection conn, String courseID) {
        String departmentID = "";
        System.out.println("正在查找" + courseID + "课程的部门编号");
        String sql = SqlSentence.GET_DEPARTMENT_ID_BY_COURSE_ID + "'" + courseID + "'";
//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                departmentID = rs.getString("departmentID").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        System.out.println(employeeID);
        return departmentID;
    }
}
