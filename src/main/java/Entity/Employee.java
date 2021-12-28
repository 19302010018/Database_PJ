package Entity;

import Relation.Take;
import Relation.Teach;
import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class Employee {

    //员工维护个人信息
    public static void updateEmployeeMsg(Connection conn, String employeeID, HashMap msgs) {
        System.out.println("正在更新员工信息");

        HashMap limits = new HashMap();
        limits.put("employeeID", employeeID);
        String sql = SqlSentence.UPDATE_EMPLOYEE_MSG + SqlSentence.updateClauseGenerator(msgs) + SqlSentence.whereClauseGenerator(limits);

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public static void checkGrades(Connection conn, String employeeID) {
        System.out.println("正在查找" + employeeID + "的员工成绩");
        HashMap limits = new HashMap();
        limits.put("employeeID", employeeID);
        limits.put("finished", 1);
        String sql = SqlSentence.GET_EXAM + SqlSentence.whereClauseGenerator(limits);

        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                num++;
                String id = rs.getString("id").trim();
                String courseID = rs.getString("courseID").trim();
                String grade = rs.getString("grade").trim();
                System.out.println(id + "  |  "
                        + courseID + "  |  " +
                        grade);
            }
            if (num == 0) {
                System.out.println("还没有参加过任何考试哦");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkCourses(Connection conn, String employeeID) {
        System.out.println("正在查找" + employeeID + "的课程信息");
        HashMap limits = new HashMap();
        limits.put("employeeID", employeeID);
        String sql = SqlSentence.GET_EXAM + SqlSentence.whereClauseGenerator(limits);

        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                num++;
                String id = rs.getString("id").trim();
                String courseID = rs.getString("courseID").trim();
                String grade = rs.getString("grade").trim();
                String status = rs.getString("status").trim();
                String teacherID = Teach.getTeacherIDByCourseID(conn, rs.getString("courseID").trim());
                String teacherName = getEmployeeNameByID(conn, teacherID);
                String teachTime = Teacher.getTeachTimeByID(conn, teacherID);
                System.out.println(id + "  |  "
                        + courseID + "  |  "
                        + grade + "  |  "
                        + status + "  |  "
                        + teacherID + "  |  "
                        + teacherName + "  |  "
                        + teachTime
                );

            }
            if (num == 0) {
                System.out.println("还没有参加过任何考试哦");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getEmployeeIDByName(Connection conn, String employeeName) {
        String employeeID = "";
        System.out.println("正在查找本部门中" + employeeName + "的员工ID");
        String sql = SqlSentence.GET_ID_BY_EMPLOYEE_NAME + "'" + employeeName + "'";
//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                employeeID = rs.getString("employeeID").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        System.out.println(employeeID);
        return employeeID;

    }

    public static String getEmployeeNameByID(Connection conn, String employeeID) {
        String employeeName = "";
        System.out.println("正在查找本部门中" + employeeID + "的员工姓名");
        String sql = SqlSentence.GET_NAME_BY_EMPLOYEE_ID + "'" + employeeID + "'";
//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                employeeName = rs.getString("employeeName").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        System.out.println(employeeID);
        return employeeName;

    }


    public static String getDepartmentIDByEmployeeID(Connection conn, String employeeID) {
        String departmentID = "";
        System.out.println("正在查找" + employeeID + "的部门编号");
        String sql = SqlSentence.GET_DEPARTMENT_ID_BY_EMPLOYEE_ID + "'" + employeeID + "'";
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

    //任何人登录时自动分配必修课程
    public static void autoAssignMandatoryCourse(Connection conn) {
        Take.autoAssignMandatoryCourse(conn);

    }

    public static int executeSQL(Connection conn, String sql) {
        int num = 0;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                num++;
                String employeeID = rs.getString("employeeID").trim();
                String employeeName = rs.getString("employeeName").trim();
                String sex = rs.getString("sex").trim();
                int age = rs.getInt("age");
                String entryTime = rs.getString("entryTime").trim();
                String address = rs.getString("address").trim();
                String telephone = rs.getString("telephone").trim();
                String email = rs.getString("email").trim();

                System.out.println(employeeID + "  |  " +
                        employeeName + "  |  " +
                        sex + "  |  " +
                        age + "  |  " +
                        entryTime + "  |  " +
                        address + "  |  " +
                        telephone + "  |  " +
                        email);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }

    public static ArrayList<String> getEmployeeIDByDepartmentID(Connection conn, String departmentID) {
        ArrayList<String> employees = new ArrayList<>();
        System.out.println("正在查找" + departmentID + "部门的所有员工的员工号");
        String sql = SqlSentence.GET_EMPLOYEE_ID_BY_DEPARTMENT_ID + "'" + departmentID + "'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                employees.add(rs.getString("employeeID").trim());
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return employees;
    }

    public static Boolean checkIsTeacher(Connection conn, String employeeID) {
        System.out.println("正在检查" + employeeID + "是不是teacher");
        HashMap limits = new HashMap();
        limits.put("employeeID", employeeID);

        String sql = SqlSentence.CHECK_IS_TEACHER + SqlSentence.whereClauseGenerator(limits);

//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                num++;

            }
            if (num == 0) {
                System.out.println("不是老师");
                return false;
            } else {
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static Boolean checkIsManager(Connection conn, String employeeID) {
        System.out.println("正在检查" + employeeID + "是不是manager");
        HashMap limits = new HashMap();
        limits.put("employeeID", employeeID);

        String sql = SqlSentence.CHECK_IS_MANAGER + SqlSentence.whereClauseGenerator(limits);
//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                num++;

            }
            if (num == 0) {
                System.out.println("不是部门主管");
                return false;
            } else {
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
