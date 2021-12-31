package Entity;

import Relation.Take;
import Util.SqlSentence;
import org.apache.ibatis.javassist.CodeConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Teacher extends Employee {

    public Teacher(Connection conn, String name) {
        super(conn, name);
    }



    public static void getStudents(Connection conn, String teacherID) {
        ArrayList<Course> courseArrayList = Teacher.getCourses(conn, teacherID);
        ArrayList<String> courses = new ArrayList<>();
        for(Course course:courseArrayList){
            courses.add(course.getCourseID());
        }

        ArrayList<String> departmentIDs = new ArrayList<>();

        System.out.println("正在查找" + teacherID + "教授的所有学生的信息");
        for (int i = 0; i < courses.size(); i++) {

            ArrayList<String> students = new ArrayList<>();
            departmentIDs.add(Course.getDepartmentIDByCourseID(conn, courses.get(i)));
            String sql = SqlSentence.GET_EMPLOYEE_ID_BY_COURSE_ID + "'" + courses.get(i) + "'";
            PreparedStatement pstmt;
            try {
                pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    students.add(rs.getString("employeeID").trim());
                }

                System.out.println("课程" + courses.get(i) + "的学生信息如下：");
                for (int j = 0; j < students.size(); j++) {
                    getEmployeeMsgByID(conn, departmentIDs.get(i), students.get(j));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }


    //获取这个老师教的所有课程
    public static ArrayList<Course> getCourses(Connection conn, String teacherID) {
        ArrayList<Course> courses = new ArrayList<>();
        System.out.println("正在查找" + teacherID + "教授的所有课程");
        String sql = SqlSentence.GET_COURSES_BY_TEACHER_ID + "'" + teacherID + "'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Course course = new Course(conn,rs.getString("courseID").trim());
                courses.add(course);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return courses;
    }

    public static ArrayList<Employee> getEmployeeMsgByID(Connection conn, String departmentID, String employeeID) {
        System.out.println("正在查找本部门中ID为" + employeeID + "的员工信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        limits.put("employeeID", employeeID);
        String sql = SqlSentence.EMPLOYEE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        ArrayList<Employee> list = executeSQL(conn, sql);
        int num = list.size();
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }
        return list;
    }

    //给学生登分
    public static void registerGrades(Connection conn, String employeeID, String courseID, int grade) {
        Take.registerGrades(conn, employeeID, courseID, grade);
    }

    public static String getTeachTimeByID(Connection conn, String employeeID) {
        String teachTime = "";
        System.out.println("正在查找" + employeeID + "的教师任教时间");
        String sql = SqlSentence.GET_TEACH_TIME_BY_ID + "'" + employeeID + "'";
//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                teachTime = rs.getString("teachTime").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        System.out.println(employeeID);
        return teachTime;

    }

    public static void addCourse(Connection conn, String teacherID,String courseID, String name, String type,
                                 String syllabus, String departmentID, int mandatory) {
        //增加course表
        LinkedHashMap msgs = new LinkedHashMap();
        msgs.put("courseID", courseID);
        msgs.put("name", name);
        msgs.put("type", type);
        msgs.put("syllabus", syllabus);

        String sql1 = SqlSentence.ADD_COURSE + SqlSentence.insertClauseGenerator(msgs);

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            pstmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //增加courseBelong表
        LinkedHashMap msgs2 = new LinkedHashMap();
        msgs2.put("courseID", courseID);
        msgs2.put("departmentID", departmentID);
        msgs2.put("mandatory", mandatory);

        String sql2 = SqlSentence.ADD_COURSE_BELONG + SqlSentence.insertClauseGenerator(msgs2);
        try {
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //增加teach表
        LinkedHashMap msgs3 = new LinkedHashMap();
        msgs3.put("teacherID",teacherID);
        msgs3.put("courseID", courseID);

        String sql3 = SqlSentence.ADD_TEACH + SqlSentence.insertClauseGenerator(msgs3);
//        System.out.println(sql3);
        try {
            PreparedStatement pstmt3 = conn.prepareStatement(sql3);
            pstmt3.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("成功了哦！");

    }
}
