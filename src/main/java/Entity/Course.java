package Entity;

import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Course {
    String courseID;
    String name;
    String type;
    String syllabus;

    public Course(Connection conn,String courseID){
        String sql = "select * from course where courseID = '" + courseID+"'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                this.courseID = rs.getString("courseID").trim();
                this.name = rs.getString("name").trim();
                this.type = rs.getString("type").trim();
                this.syllabus = rs.getString("syllabus").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Course(String courseID, String name, String type, String syllabus) {
        this.courseID = courseID;
        this.name = name;
        this.type = type;
        this.syllabus = syllabus;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID='" + courseID + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", syllabus='" + syllabus + '\'' +
                '}';
    }

    //部门主管查看本部门培训课程信息
    public static ArrayList<Course> getCourseMsg(Connection conn, String departmentID) {
        System.out.println("正在查找本部门所有的培训课程信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        String sql = SqlSentence.COURSE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        ArrayList<Course> list = executeSQL(conn, sql);
        int num = list.size();
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }

        return list;

    }

    public static ArrayList<Course> executeSQL(Connection conn, String sql) {
        int num = 0;
        PreparedStatement pstmt;
        ArrayList<Course> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                num++;
                String courseID = rs.getString("courseID").trim();
                String name = rs.getString("name").trim();
                String type = rs.getString("type").trim();
                String syllabus = rs.getString("syllabus").trim();
                Course course = new Course(courseID,name,type,syllabus);
                System.out.println(course.toString());
                list.add(course);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
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
