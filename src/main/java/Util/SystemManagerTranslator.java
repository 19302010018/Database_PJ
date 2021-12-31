package Util;

import Entity.*;
import Relation.Take;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemManagerTranslator extends ArgTranslator{


    public SystemManagerTranslator(String employeeID, Connection conn) {
        super(employeeID,conn);
    }

    public void help(){
        System.out.println("You can:" +
                "1.add employee \n" +
                "2.delete employee [employeeID] \n" +
                "3.update employee [] \n" +
                "4.get employee [employeeID] \n" +
                "5.add course \n" +
                "6.delete course [courseID] \n" +
                "7.update course  \n" +
                "8.get course [courseID] \n" +
                "9.get score [employeeID/Name] \n" +
                "10.get log \n" +
                "11.delete log \n");
    }
    @Override
    public void translate(String cmd) {
        System.out.println("system manager cmd");
        String[] args = cmd.split(" ");
        switch (args[0].trim()){
            case "get":
                get(args);
                break;
            case "update":
                update(args);
                break;
            case "help":
                help();
                break;
            case "exit":
                break;
            case "add":
                add(args);
                break;
            case "delete":
                delete(args);
            default:
                System.out.println("要不要输入一个help先");
                break;

        }
    }

    public void get(String[] args){
        String option = args[1];
        switch (option){
            case "employee":
                if(args[2].length()<10){
                    System.out.println(new Employee(conn,args[2]));
                }else{
                    System.out.println(Employee.getEmployee(conn,args[2]));
                }
                break;
            case "course":
                System.out.println(new Course(conn,args[2]));
                break;
            case "score":
                ArrayList<Take> takes = Take.getTakeByID(conn,args[2]);
                for(Take take : takes){
                    System.out.println(take.toString());
                }
                break;
            case "log":
                List<Log> logs = Log.getLogs(conn);
                for(Log log:logs){
                    System.out.println(log.toString());
                }
                break;
        }
    }
    public void update(String[] args){
        String option = args[1];
        switch (option){
            case "employee":
                HashMap<String,String> map = new HashMap();
                map.put("age",args[3]);
                map.put("address",args[4]);
                map.put("telephone",args[5]);
                map.put("email",args[6]);
                Employee.updateEmployeeMsg(conn,args[2],map);
                break;
            case "course":
                int flag = 2;
                String courseID = args[flag++];
                String name = args[flag++];
                String type = args[flag++];
                String syllabus = args[flag];
                Teacher.updateCourse(conn,courseID,name,type,syllabus);
                break;
        }
    }
    public void add(String[] args){
        String option = args[1];
        switch (option){
            case "employee":
                int flag = 2;
                SystemManager.addEmployee(conn,args[flag++],args[flag++],args[flag++],Integer.parseInt(args[flag++]),args[flag++],args[flag++],args[flag++],args[flag++],args[flag]);
                break;
            case "course":
                flag = 2;
                Teacher.addCourse(conn,args[flag++],args[flag++],args[flag++],args[flag++],args[flag++],args[flag++],Integer.parseInt(args[flag++])==1?1:0);
                break;
        }
    }
    public void delete(String[] args){
        String option = args[1];
        switch (option){
            case "employee":
                SystemManager.deleteEmployee(conn,args[2]);
                break;
            case "course":
                SystemManager.deleteCourse(conn,args[2]);
                break;
            case "log":
                Log.deleteLog(conn,Integer.parseInt(args[2]));
                break;
        }
    }
}
