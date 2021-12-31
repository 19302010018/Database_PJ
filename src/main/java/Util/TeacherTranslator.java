package Util;

import Entity.Course;
import Entity.Employee;
import Entity.Teacher;

import java.sql.Connection;
import java.util.ArrayList;
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
                "5. update course \n then input [courseID] [name] [type] [syllabus] [mandatory(必修填1否则填0)] "+
                "6. get students \n"+
                "7. update score \n then input [studentID] [courseID] [score] \n"+
                "8. get courses \n"+
                "9. insert [courseID] [name] [type] [syllabus] [mandatory(必修填1否则填0)] \n"+
                "10. exit"
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
            case "course":
                updateCourse(args,2);
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

    public void updateCourse(String[] args,int flag){
        String courseID = args[flag++];
        String name = args[flag++];
        String type = args[flag++];
        String syllabus = args[flag++];
//        int mandatory = Integer.parseInt(args[flag])==1?1:0;
        ArrayList<Course> courses = Teacher.getCourses(conn,me.getEmployeeID());
        for(Course course:courses){
            if(courseID.equals(course.getCourseID())){
                Teacher.updateCourse(conn,courseID,name,type,syllabus);
                return;
            }
        }
        System.out.println("手别伸这么长！不是你的课你别管！");

    }

    public void insert(String[] args){
        insertCourse(args,1);
    }
    public void insertCourse(String[] args,int flag){
        String departmentID = Employee.getDepartmentIDByEmployeeID(conn,me.getEmployeeID());
        Teacher.addCourse(conn,me.getEmployeeID(),args[flag++],args[flag++],args[flag++],args[flag++],departmentID,Integer.parseInt(args[flag])==1?1:0);
    }



}
