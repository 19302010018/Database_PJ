package Relation;

import Entity.Employee;
import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Take {
    int id;
    String employeeID;
    String courseID;
    boolean is_finish;
    int grade;
    boolean is_pass;

    public Take(String employeeID, String courseID, boolean is_finish){
        this.employeeID = employeeID;
        this.courseID = courseID;
        this.is_finish = is_finish;
    }

    public Take() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public boolean isIs_finish() {
        return is_finish;
    }

    public void setIs_finish(boolean is_finish) {
        this.is_finish = is_finish;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isIs_pass() {
        return is_pass;
    }

    public void setIs_pass(boolean is_pass) {
        this.is_pass = is_pass;
    }

    @Override
    public String toString() {
        return "Take{" +
                "id=" + id +
                ", employeeID='" + employeeID + '\'' +
                ", courseID='" + courseID + '\'' +
                ", is_finish=" + is_finish +
                ", grade=" + grade +
                ", is_pass=" + is_pass +
                '}';
    }



    public static void insertTake(Connection conn, Take take){
        LinkedHashMap msgs = new LinkedHashMap();
        msgs.put("employeeID", take.employeeID);
        msgs.put("courseID", take.courseID);
        msgs.put("finished", take.is_finish);
        String sql = SqlSentence.ASSIGN_COURSE + SqlSentence.insertClauseGenerator(msgs);
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void assignCoursesByEmployeeID(Connection conn, String employeeID, String courseID) {
        if (CourseBelong.checkCourseMandatory(conn, courseID)) {
            //是必修课
            System.out.println("这门课是必修课，不需要分配");
            return;
        }
        System.out.println("正在为员工" + employeeID + "分配课程" + courseID);
        Take take = new Take(employeeID,courseID,false);
        insertTake(conn,take);

    }

    public static void assignCoursesByEmployeeName(Connection conn, String employeeName, String courseID) {
        if (CourseBelong.checkCourseMandatory(conn, courseID)) {
            //是必修课
            System.out.println("这门课是必修课，不需要分配");
            return;
        }
        System.out.println("正在为员工" + employeeName + "分配课程" + courseID);
        String employeeID = new Employee(conn,employeeName).getEmployeeID();
        Take take = new Take(employeeID,courseID,false);
        insertTake(conn,take);

    }

    public static void autoAssignMandatoryCourse(Connection conn) {
        ArrayList<String> courses = new ArrayList<>();
        ArrayList<String> departments = new ArrayList<>();
        System.out.println("正在自动分配必修课程");
        String sql = SqlSentence.GET_MANDATORY_COURSES;
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

                Take take = new Take(employee,course,false);
                insertTake(conn,take);

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


    /**
     * get Take by employee id
     * @param conn connection
     * @param employeeID employeeid
     * @return take
     */
    public static ArrayList<Take> getTakeByID(Connection conn,String employeeID){
        String sql = SqlSentence.GET_TAKE_BY_EMPLOYEE_ID + "'"+employeeID+"'";
        ArrayList<Take> list= new ArrayList<>();

        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Take take = new Take();
                take.setEmployeeID(employeeID);
                take.setId(rs.getInt("id"));
                take.setCourseID(rs.getString("courseID").trim());
                take.setIs_finish(rs.getBoolean("finished"));
                take.setGrade(rs.getInt("id"));
                take.setIs_pass(rs.getBoolean("status"));
                list.add(take);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        System.out.println(take.toString());
        return list;
    }

}




