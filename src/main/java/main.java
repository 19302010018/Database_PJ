import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import Entity.Course;
import Entity.Employee;
import Entity.SystemManager;
import Entity.Teacher;
import Relation.Take;
import Relation.Teach;
import Util.SqlSentence;
import com.sun.xml.internal.ws.message.EmptyMessageImpl;
import org.apache.ibatis.jdbc.ScriptRunner;

public class Main {
    private static final String username = "root";
    private static final String password = "12345Aa?";
    private static final String url = "jdbc:mysql://47.101.188.143:3306/dbpj";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("注册驱动成功");
        } catch (ClassNotFoundException e) {
            System.out.println("注册驱动失败");
            e.printStackTrace();
            return;
        }
        Connection conn = null;


        try {
            //TODO 根据登录的人确定能访问的函数、能看到的人(姓名和ID都建个表)


            conn = DriverManager.getConnection(url, username, password);
            SystemManager.getCourseMsg(conn,"35142");
//            SystemManager.updateCourse(conn, "35142", "数据库设计2", "新type", "sajbdhjhfusdh fsdfk");
//            Teacher.addCourse(conn,"a","aa","aaa","asasa","2",0 );

//            System.out.println(Employee.checkIsTeacher(conn,"10231106004"));
//            System.out.println(Employee.checkIsManager(conn,"10231106004"));
//            SystemManager.addEmployee(conn, "22222222222", "张二",
//                    "男", 21, "2021-11-23", "北京", "11111111110"
//                    , "example@yy.com","2");
//            System.out.println(Teach.getTeacherIDByCourseID(conn,"35155"));
//            Employee.checkCourses(conn,"10231106003");
//            Employee.checkGrades(conn,"10231106003");
//            Take.registerGrades(conn, "10231106002", "35155", 50);

//            Take.autoAssignMandatoryCourse(conn);
//            Course.getCourseMsg(conn,"10");
//            System.out.println(Teacher.getCourses(conn,"10231106124"));
//            Teacher.getStudents(conn,"10231106124");

//            Take.assignCoursesByEmployeeID(conn, "10231106003", "35142");
//            Take.assignCoursesByEmployeeName(conn, "王鑫", "35142");
//            ScriptRunner runner = new ScriptRunner(conn);
//
//            Employee.getEmployeeMsg(conn, "10");
//            System.out.println();
//            Employee.getEmployeeMsgByID(conn, "10", "10231106003");
//            System.out.println();
//            Employee.getEmployeeMsgByName(conn, "10", "蒋玥");
//            HashMap msgs = new HashMap();
//            msgs.put("age", 6);
//            msgs.put("address","贵州");
//            msgs.put("telephone","11111111111");
//            msgs.put("email","example@yy.com");
//
//            Employee.updateEmployeeMsg(conn,"10231106003",msgs);
//            Employee.getEmployeeMsg(conn, "10");


        } catch (Exception e) {
            System.out.println("wrong");
            e.printStackTrace();
        }

    }

}


