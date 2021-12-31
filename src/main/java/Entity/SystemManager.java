package Entity;

import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SystemManager extends Employee {

    public SystemManager(Connection conn, String name) {
        super(conn, name);
    }

    //增加employee
    public static void addEmployee(Connection conn, String employeeID, String employeeName,
                                   String sex, int age, String entryTime, String address,
                                   String telephone, String email, String departmentID) {

        //增加employee表
        LinkedHashMap msgs = new LinkedHashMap();
        msgs.put("employeeID", employeeID);
        msgs.put("employeeName", employeeName);
        msgs.put("sex", sex);
        msgs.put("age", age);
        msgs.put("entryTime", entryTime);
        msgs.put("address", address);
        msgs.put("telephone", telephone);
        msgs.put("email", email);

        String sql1 = SqlSentence.ADD_EMPLOYEE + SqlSentence.insertClauseGenerator(msgs);

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            pstmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //增加employeeBelong表
        LinkedHashMap msgs2 = new LinkedHashMap();
        msgs2.put("employeeID", employeeID);
        msgs2.put("departmentID", departmentID);
        String sql2 = SqlSentence.ADD_EMPLOYEE_BELONG + SqlSentence.insertClauseGenerator(msgs2);
        try {
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    //删除employee
    public static void deleteEmployee(Connection conn, String employeeID) {
        //判断这个人是不是部门主管/教师
        boolean isTeacher = checkIsTeacher(conn, employeeID);
        boolean isManager = checkIsManager(conn, employeeID);

        //删employee表
        String deleteEmployee = SqlSentence.DELETE_EMPLOYEE + "'" + employeeID + "'";
        try {
            PreparedStatement pstmt1 = conn.prepareStatement(deleteEmployee);
            pstmt1.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //删employeeBelong表
        String deleteEmployeeBelong = SqlSentence.DELETE_EMPLOYEE_BELONG + "'" + employeeID + "'";
        try {
            PreparedStatement pstmt2 = conn.prepareStatement(deleteEmployeeBelong);
            pstmt2.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //删take表
        String deleteTake = SqlSentence.DELETE_TAKE_BY_EMPLOYEE_ID + "'" + employeeID + "'";
        try {
            PreparedStatement pstmt3 = conn.prepareStatement(deleteTake);
            pstmt3.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (isTeacher) {
            //老师删teacher表
            String deleteTeacher = SqlSentence.DELETE_TEACHER + "'" + employeeID + "'";
            try {
                PreparedStatement pstmt4 = conn.prepareStatement(deleteTeacher);
                pstmt4.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //老师删teach表
            String deleteTeach = SqlSentence.DELETE_TEACH + "'" + employeeID + "'";
            try {
                PreparedStatement pstmt5 = conn.prepareStatement(deleteTeach);
                pstmt5.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //老师删相关course表
            ArrayList<Course> courseArrayList = Teacher.getCourses(conn, employeeID);
            ArrayList<String> courses = new ArrayList<>();
            for(Course course:courseArrayList){
                courses.add(course.getCourseID());
            }
            for (int i = 0; i < courses.size(); i++) {
                String deleteCourse = SqlSentence.DELETE_COURSE + "'" + courses.get(i) + "'";
                try {
                    PreparedStatement pstmt6 = conn.prepareStatement(deleteCourse);
                    pstmt6.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            //course删了导致take也要删
            for (int i = 0; i < courses.size(); i++) {
                String deleteTakeByCourse = SqlSentence.DELETE_TAKE_BY_COURSE_ID + "'" + courses.get(i) + "'";
                try {
                    PreparedStatement pstmt8 = conn.prepareStatement(deleteTakeByCourse);
                    pstmt8.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            //老师删相关courseBelong表
            for (int i = 0; i < courses.size(); i++) {
                String deleteCourseBelong = SqlSentence.DELETE_COURSE_BELONG + "'" + courses.get(i) + "'";
                try {
                    PreparedStatement pstmt7 = conn.prepareStatement(deleteCourseBelong);
                    pstmt7.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        if (isManager) {
            //管理员删manager
            String deleteManager = SqlSentence.DELETE_MANAGER + "'" + employeeID + "'";
            try {
                PreparedStatement pstmt9 = conn.prepareStatement(deleteManager);
                pstmt9.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }


    public static Employee getEmployeeByName(Connection conn,String employeeName){
        return new Employee(conn,employeeName);
    }
    public static Employee getEmployeeById(Connection conn,String employeeID){
        return getEmployee(conn,employeeID);
    }

    /**
     * 修改员工信息
     * @param conn
     * @param employeeID
     * @param msgs
     */
    public static void alterEmployee(Connection conn,String employeeID, HashMap msgs){
        updateEmployeeMsg(conn,employeeID,msgs);
    }

    //增加course信息调用teacher方法

    //删除course
    public static void deleteCourse(Connection conn, String courseID) {
        //删teach表
        String deleteTeach = SqlSentence.DELETE_TEACH_BY_COURSE + "'" + courseID + "'";
        try {
            PreparedStatement pstmt1 = conn.prepareStatement(deleteTeach);
            pstmt1.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //删take表
        String deleteTake = SqlSentence.DELETE_TAKE_BY_COURSE_ID + "'" + courseID + "'";
        try {
            PreparedStatement pstmt4 = conn.prepareStatement(deleteTake);
            pstmt4.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //删course表
        String deleteCourse = SqlSentence.DELETE_COURSE + "'" + courseID + "'";
        try {
            PreparedStatement pstmt2 = conn.prepareStatement(deleteCourse);
            pstmt2.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //删courseBelong表
        String deleteCourseBelong = SqlSentence.DELETE_COURSE_BELONG + "'" + courseID + "'";
        try {
            PreparedStatement pstmt3 = conn.prepareStatement(deleteCourseBelong);
            pstmt3.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //查找course
    public static void getCourseMsg(Connection conn, String courseID) {
        System.out.println("正在查找ID为" + courseID + "的课程信息");
        HashMap limits = new HashMap();
        limits.put("courseID", courseID);
        String sql = SqlSentence.COURSE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        int num = 0;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                num++;
                String name = rs.getString("name").trim();
                String type = rs.getString("type").trim();
                String syllabus = rs.getString("syllabus").trim();
                String departmentID = rs.getString("departmentID").trim();
                String mandatory = rs.getString("mandatory").trim();

                System.out.println(name + "  |  " +
                        type + "  |  " +
                        syllabus + "  |  " +
                        departmentID + "  |  " +
                        mandatory);
            }
            if (num==0){
                System.out.println("没有查到这一门课程");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    //根据姓名/员工号查询员工信息/培训成绩可以复用Manager的方法


}
