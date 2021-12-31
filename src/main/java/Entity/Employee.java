package Entity;

import Relation.Take;
import Relation.Teach;
import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Employee {

    String employeeID;
    String employeeName;
    String sex;
    int age;
    Date entryTime;
    String address;
    String telephone;
    String email;
    Connection conn;

    public Employee() {

    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID='" + employeeID + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", entryTime=" + entryTime +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Employee(Connection conn, String name){
        this.conn = conn;
        String sql = SqlSentence.GET_EMPLOYEE_BY_EMPLOYEE_NAME + "'" + name + "'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                this.employeeID = rs.getString("employeeID").trim();
                this.employeeName = rs.getString("employeeName").trim();
                this.sex = rs.getString("sex").trim();
                this.age = rs.getInt("age");
                this.address = rs.getString("address").trim();
                this.entryTime = rs.getDate("entryTime");
                this.telephone = rs.getString("telephone").trim();
                this.email = rs.getString("email").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        System.out.println(this.toString());
    }

    public Employee(String employeeID, String employeeName, String sex, int age, Date entryTime, String address, String telephone, String email) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.sex = sex;
        this.age = age;
        this.entryTime = entryTime;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
    }

    public static Employee getEmployee(Connection conn,String employeeID){
        Employee employee = new Employee();
        String sql = "select * from employee where employeeID = '" + employeeID + "'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                employee.employeeID = rs.getString("employeeID").trim();
                employee.employeeName = rs.getString("employeeName").trim();
                employee.sex = rs.getString("sex").trim();
                employee.age = rs.getInt("age");
                employee.address = rs.getString("address").trim();
                employee.entryTime = rs.getDate("entryTime");
                employee.telephone = rs.getString("telephone").trim();
                employee.email = rs.getString("email").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employee;
    }
    public static boolean checkIsEmployee(Connection conn,String employeeID){
        Employee employee = getEmployee(conn,employeeID);
        return employee.getEmployeeID() != null;
    }

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

    public static ArrayList<Employee> executeSQL(Connection conn, String sql) {
        int num = 0;
        PreparedStatement pstmt;
        ArrayList<Employee> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                num++;
                String employeeID = rs.getString("employeeID").trim();
                String employeeName = rs.getString("employeeName").trim();
                String sex = rs.getString("sex").trim();
                int age = rs.getInt("age");
                Date entryTime = rs.getDate("entryTime");
                String address = rs.getString("address").trim();
                String telephone = rs.getString("telephone").trim();
                String email = rs.getString("email").trim();

                Employee employee = new Employee(employeeID,employeeName,sex,age,entryTime,address,telephone,email);
                System.out.println(employee.toString());

                list.add(employee);

            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
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
