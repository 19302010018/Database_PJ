package Util;

import Entity.Course;
import Entity.Employee;
import Entity.Manager;
import Entity.Teacher;
import Relation.EmployeeBelong;
import Relation.Take;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagerTranslator extends EmployeeTranslator{
    String departmentID;
    public ManagerTranslator(String employeeID, Connection conn) {
        super(employeeID,conn);
        this.departmentID = Employee.getDepartmentIDByEmployeeID(conn,employeeID);
    }
    //TODO 增加用户信息（招人HR）删人（炒鱿鱼） 修改员工信息 查看员工 查看部门的培训课程 分配课程x2 查成绩x2 转人x2 status查人 课程类型查找 查询符合转人情况
    //TODO 转老师可能有问题

    @Override
    public void help() {
        System.out.println("您能进行的操作有：\n" +
                "1. get me \n"+
                "2. get score \n"+
                "3. get course \n"+
                "4. update me [age] [address] [telephone] [email] \n"+
           //     "5. insert employee [] \n" +
           //     "6. delete employee [employeeID] \n" +
                "7. update employee [age] [address] [telephone] [email] \n" +
                "8. get employee [employeeID/Name] \n" +
                "9. get departmentCourse \n" +
                "10. update courseBelong [employeeID/Name] [courseID]\n" +
                "11. get employeeScore [employeeID/Name] \n" +
                "12. update employeeToNewDepartment \n" +
                "13. get particularTake [键值对] \n" +
                "14. get changeStandard \n" +
                "15. get ifFitStandard [employeeID/Name] \n" +
                "16. get employees \n" +
                "17. get takePassTime [通过与否true/false] [次数] "+
                "18. exit");
    }

    @Override
    public void translate(String cmd) {
        System.out.println("teacher cmd");
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
            default:
                System.out.println("要不要输入一个help先");
                break;

        }
    }


    public void get(String[] args){
        String option = args[1];
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
            case "employee"://看看你个傻逼是谁
                System.out.println(getEmployee(args[2]));
                break;
            case "departmentCourse":
                getDepartmentCourse(args[2]);
                break;
            case "employeeScore":
                getEmployeeScore(args[2]);
                break;
            case "particularTake":
                getParticularTake(args);
                break;
            case "changeStandard":
                getChangeStandard();
                break;
            case "ifFitStandard":
                getIfFitStandard(args[2]);
                break;
            case "employees":
                Manager.getEmployeeMsg(conn,departmentID);
                break;
            case "takePassTime":
                for(Employee employee:Manager.getEmployeeSpec(conn,Boolean.parseBoolean(args[2]),Integer.parseInt(args[3]),departmentID)){
                    System.out.println(employee.toString());
                }
                break;
        }
    }
    public void getMyDetail(){
        System.out.println(me.toString());
    }
    public void getScore(){
        Employee.checkGrades(conn,me.getEmployeeID());
    }
    public Employee getEmployee(String arg){

        if(arg.length()<10){
            return new Employee(conn,arg);
        }else{
            return Employee.getEmployee(conn,arg);
        }

    }
    public void getDepartmentCourse(String arg){
        ArrayList<Course> courses = Manager.getCourseMsg(conn,departmentID);
        for(Course course:courses){
            System.out.println(course.toString());
        }
    }
    public void getEmployeeScore(String arg){
        Employee employee = arg.length()<10?new Employee(conn,arg):Employee.getEmployee(conn,arg);

        ArrayList<Take> takes = Take.getTakeByID(conn,employee.getEmployeeID());
        for(Take take:takes){
            System.out.println(take.toString());
        }

    }
    private void getParticularTake(String[] args) {
        if(args.length % 2 != 0) {
            System.out.println("Wrong args!Try again!");
            return;
        }
        HashMap<String,Object> limit = new HashMap<>();
        for(int i = 2;i < args.length;i+=2){
            switch(args[i]) {
                case "pass":
                case "finish":
                case "courseID":
                case "type":
                    limit.put(args[i],args[i+1]);
                    break;
                default:
                    System.out.println("Wrong args!Try again!");
                    return;
            }
        }
        ArrayList<Take> takes = Manager.getTakes(conn,limit,departmentID);
        for(Take take:takes){
            System.out.println(take.toString());
        }
    }
    public void getChangeStandard(){
        ArrayList<EmployeeBelong> fitPairs = Manager.checkDepartment(conn,departmentID);
        for(EmployeeBelong fitPair:fitPairs){
            System.out.println("部门: " + fitPair.getDepartmentID() + " 员工:" + fitPair.getEmployeeID());
        }
    }
    public void getIfFitStandard(String arg){
        Employee employee = arg.length()<10?new Employee(conn,arg):Employee.getEmployee(conn,arg);
        boolean flag = Manager.checkChangeDepartmentStatusById(conn,employee.getEmployeeID(),departmentID);
        String out = flag?"可以转部门":"不可以";
        System.out.println(out);

    }

    public void update(String[] args){
        switch (args[1]){
            case "me":
                updateMyDetail(args,2);
                break;
            case "employee":
                HashMap<String,String> map = new HashMap();
                map.put("age",args[3]);
                map.put("address",args[4]);
                map.put("telephone",args[5]);
                map.put("email",args[6]);
                Employee.updateEmployeeMsg(conn,args[2],map);
                break;
            case "courseBelong":
                if(args[2].length()<10){
                    Manager.assignCoursesByEmployeeName(conn,args[2],args[3]);
                }else{
                    Manager.assignCoursesByEmployeeName(conn,args[2],args[3]);
                }
                break;
            case "employeeToNewDepartment":
                if(args[2].length()<10){
                    Manager.changeDepartmentByName(conn,args[3],args[2]);
                }else{
                    Manager.changeDepartmentByID(conn,args[3],args[2]);
                }
                break;
            default:
                System.out.println("Wrong args");
                break;
        }
    }


}
