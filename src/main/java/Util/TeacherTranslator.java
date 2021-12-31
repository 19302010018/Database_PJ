package Util;

import Entity.Course;
import Entity.Employee;
import Entity.Teacher;

import java.sql.Connection;
import java.util.HashMap;

public class TeacherTranslator extends EmployeeTranslator{


    public TeacherTranslator(String employeeID, Connection conn) {

        super(employeeID,conn);

    }
    @Override
    public void help() {
        System.out.println("您能进行的操作有：\n" +
                "1. get me \n"+
                "2. get score \n"+
                "3. get course \n"+ //TODO get course 的nullpointer处理
                "4. update me \n then input [age] [address] [telephone] [email]\n"+
                "5. get students \n"+
                "6. update score \n then input [studentID] [courseID] [score] \n"+
                "7. get courses \n"+
                "8. insert [courseID] [name] [type] [syllabus] [mandatory(必修填1否则填0)] \n"+
                "9. exit"
        );
    }

    @Override
    public void translate(String cmd) {
        System.out.println("teacher cmd");
        String[] args = cmd.split(" ");
        switch (args[0].trim()){
            case "get":
                get(args[1]);
                break;
            case "update":
                update(args);
                break;
            case "help":
                help();
                break;
            case "exit":
                break;
            case "insert":
                insert(args);
                break;
            default:
                System.out.println("要不要输入一个help先");
                break;

        }
    }


    public void get(String option){
        switch (option){
            case "score":
                getScore();
                break;
            case "me":
                getMyDetail();
                break;
            case "course":
                getCourseAndTeacher();
                break;
            case "students":
                getStudents();
                break;
            case "courses":
                getCourses();
                break;
        }
    }

    public void getMyDetail(){
        System.out.println(me.toString());
    }

    public void getScore(){
        Employee.checkGrades(conn,me.getEmployeeID());
    }

    public void getStudents(){
        Teacher.getStudents(conn,me.getEmployeeID());
    }

    public void getCourses(){
        for(Course course:Teacher.getCourses(conn,me.getEmployeeID())){
            System.out.println(course.toString());
        }
    }

    public void update(String[] args){
        switch (args[1]){
            case "me":
                updateMyDetail(args,2);
                break;
            case "score":
                updateScore(args,2);
                break;
        }
    }

    public void updateScore(String[] args,int flag){
        try {
            Teacher.registerGrades(conn, args[flag++], args[flag++], Integer.parseInt(args[flag]));
        }catch (Exception e){
            System.out.println("Wrong input format!Try again");
        }
    }

    public void insert(String[] args){
        insertCourse(args,1);
    }
    public void insertCourse(String[] args,int flag){
        String departmentID = Employee.getDepartmentIDByEmployeeID(conn,me.getEmployeeID());
        Teacher.addCourse(conn,me.getEmployeeID(),args[flag++],args[flag++],args[flag++],args[flag++],departmentID,Integer.parseInt(args[flag])==1?1:0);
    }



}
