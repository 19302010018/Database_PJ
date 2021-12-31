import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

import Entity.Employee;
import Entity.Manager;
import Util.*;

public class Main {
    //TODO 同名的员工不能用name的方式修改信息
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

            SystemStart(conn);


        } catch (Exception e) {
            System.out.println("wrong");
            e.printStackTrace();
        }

    }

    static void SystemStart(Connection conn) {

        System.out.println("欢迎登录员工管理系统");
        System.out.println("请输入你的用户名与密码");
        System.out.println("由于系统在测试阶段，且pj里没要求，这次就输入用户名就可以啦");

        Scanner input = new Scanner(System.in);
        String employeeID = "";


        while (!employeeID.equals("exit")) {
            employeeID = input.nextLine();
            ArgTranslator argTranslator = null;
            switch (employeeID) {
                case "admin":
                    argTranslator = new SystemManagerTranslator(employeeID,conn);
                    break;
                default:
                    if (Employee.checkIsEmployee(conn, employeeID)) {
                        argTranslator = new EmployeeTranslator(employeeID,conn);
                        boolean isManager = Employee.checkIsManager(conn, employeeID);
                        boolean isTeacher = Employee.checkIsTeacher(conn, employeeID);
                        boolean isBoth = isManager && isTeacher;
                        if (isBoth) {
                            argTranslator = new TeacherAndManagerTranslator(employeeID,conn);
                        } else if (isTeacher) {
                            argTranslator = new TeacherTranslator(employeeID,conn);
                        } else if (isManager){
                            argTranslator = new ManagerTranslator(employeeID,conn);
                        }

                    }
                    break;
            }

            if (argTranslator != null) {
                Employee.autoAssignMandatoryCourse(conn);
                executeCmds(conn,argTranslator);
                break;
            }else {
                System.out.println("错误的用户名哦！Try again！");
            }
        }
        System.out.println("Bye~");


    }


    static void executeCmds(Connection conn,ArgTranslator argTranslator){
        System.out.println("欢迎光临！开用！");
        Scanner input = new Scanner(System.in);
        String inputLine = "";
        while(!inputLine.equals("exit")){
            inputLine = input.nextLine();
            argTranslator.translate(inputLine);
        }
    }


}


