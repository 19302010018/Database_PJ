package Util;

import Entity.Employee;

import java.sql.Connection;
import java.util.HashMap;

public class EmployeeTranslator extends ArgTranslator{
    //TODO 维护个人信息（改查自己的） 查Take和Teach和Teacher 查成绩

    /**
     *
     * [get] [option]
     * [update] 按序所有信息
     *
     */


    Employee me;
    public EmployeeTranslator(String employeeID,Connection conn) {
        super(employeeID,conn);
        me = Employee.getEmployee(conn,employeeID);
    }


    @Override
    public void help() {
        System.out.println("您能进行的操作有：\n" +
                "1. get me \n"+
                "2. get score \n"+
                "3. get course \n"+
                "4. update [age] [address] [telephone] [email]\n"+
                "5. exit");
    }

    @Override
    public void translate(String cmd) {
        System.out.println("employee cmd");
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
        }
    }

    public void getMyDetail(){
        System.out.println(me.toString());
    }

    public void getScore(){
        Employee.checkGrades(conn,me.getEmployeeID());
    }

    public void getCourseAndTeacher(){
        Employee.checkCourses(conn,me.getEmployeeID());
    }

    public void update(String[] detail){
        HashMap<String,String> map = new HashMap();
        map.put("age",detail[1]);
        map.put("address",detail[2]);
        map.put("telephone",detail[3]);
        map.put("email",detail[4]);
        Employee.updateEmployeeMsg(conn,me.getEmployeeID(),map);
    }
}
