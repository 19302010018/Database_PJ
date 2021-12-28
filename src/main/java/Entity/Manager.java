package Entity;

import Relation.CourseBelong;
import Relation.EmployeeBelong;
import Relation.Take;
import Util.SqlSentence;

import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager extends Employee {
    String departmentID;

    Manager(Connection conn, String name) {
        super(conn, name);
    }

    //部门主管查看本部门员工信息
    public static ArrayList<Employee> getEmployeeMsg(Connection conn, String departmentID) {
        System.out.println("正在查找本部门所有的员工信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        String sql = SqlSentence.EMPLOYEE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        ArrayList<Employee> list = executeSQL(conn, sql);
        int num = list.size();
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }
        return list;
    }

    //部门主管通过ID查看本部门员工信息
    public static ArrayList<Employee> getEmployeeMsgByID(Connection conn, String departmentID, String employeeID) {
        System.out.println("正在查找本部门中ID为" + employeeID + "的员工信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        limits.put("employeeID", employeeID);
        String sql = SqlSentence.EMPLOYEE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        ArrayList<Employee> list = executeSQL(conn, sql);
        int num = list.size();
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }
        return list;
    }

    //部门主管通过name查看本部门员工信息
    public static ArrayList<Employee> getEmployeeMsgByName(Connection conn, String departmentID, String name) {
        System.out.println("正在查找本部门中姓名为" + name + "的员工信息");
        HashMap limits = new HashMap();
        limits.put("departmentID", departmentID);
        limits.put("employeeName", name);
        String sql = SqlSentence.EMPLOYEE_MSG_EVERY + SqlSentence.whereClauseGenerator(limits);
        ArrayList<Employee> list = executeSQL(conn, sql);
        int num = list.size();
        if (num == 0) {
            System.out.println("对不起，没有查到相关信息");
        }
        return list;
    }

    //部门主管查看本部门培训课程信息
    public static ArrayList<Course> getCourseMsg(Connection conn, String departmentID) {
        return Course.getCourseMsg(conn, departmentID);
    }

    public static String getDepartmentIDByCourseID(Connection conn, String courseID) {
        return Course.getDepartmentIDByCourseID(conn, courseID);
    }

    //部门主管根据ID分配课程
    public static void assignCoursesByEmployeeID(Connection conn, String employeeID, String courseID) {
        Take.assignCoursesByEmployeeID(conn, employeeID, courseID);
    }

    //部门主管根据name分配课程
    public static void assignCoursesByEmployeeName(Connection conn, String employeeName, String courseID) {
        Take.assignCoursesByEmployeeName(conn, employeeName, courseID);
    }


    /**
     * 获取本部门下所有take的信息
     * @param conn
     * @param departmentID
     */
    public static ArrayList<Take> getTakesInMyDepartment(Connection conn,String departmentID){
        ArrayList<Employee> list = getEmployeeMsg(conn,departmentID);
        ArrayList<Take> result = new ArrayList<>();
        for (Employee i:list) {
            ArrayList<Take> take = Take.getTakeByID(conn,i.getEmployeeID());
//            System.out.println(take.toString());
            result.addAll(take);
        }
        return result;
    }

    /**
     * 按employeeID修改部门
     * @param conn
     * @param departmentID
     * @param employeeID
     */
    public static void changeDepartmentByID(Connection conn,String departmentID,String employeeID){
        EmployeeBelong employeeBelong = new EmployeeBelong(departmentID,employeeID);
        employeeBelong.changeDepartment(conn,employeeBelong);
    }

    /**
     * 按employeeName修改部门
     * @param conn
     * @param departmentID
     * @param employeeName
     */
    public static void changeDepartmentByName(Connection conn,String departmentID,String employeeName){
        String employeeID = new Employee(conn,employeeName).getEmployeeID();
        EmployeeBelong employeeBelong = new EmployeeBelong(departmentID,employeeID);
        employeeBelong.changeDepartment(conn,employeeBelong);
    }

    public static ArrayList<Take> getScoreByID(Connection conn,String employeeID){
        ArrayList<Take> takes = Take.getTakeByID(conn,employeeID);
        for(Take i :takes) {
            System.out.println(i.toString());
        }
        return takes;
    }

    public static ArrayList<Take> getScoreByName(Connection conn,String employeeName){
        String employeeID = new Employee(conn,employeeName).getEmployeeID();
        ArrayList<Take> takes = Take.getTakeByID(conn,employeeID);
        for(Take i :takes) {
            System.out.println(i.toString());
        }
        return takes;
    }

    /**
     * 查询特殊的takes
     * @param conn
     * @param limits
     * limits是一个HashMap，可包含pass,finish,courseID
     * @param departmentID
     * @return
     */
    public static ArrayList<Take> getTakes(Connection conn,HashMap<String,Object> limits,String departmentID){
        ArrayList<Take> takes = getTakesInMyDepartment(conn,departmentID);
        for (Take i: takes) {
            if(limits.containsKey("pass")){
                if(i.isIs_pass()!=(Boolean)limits.get("pass")){
                    takes.remove(i);
                    continue;
                }
            }
            if(limits.containsKey("finish")){
                if(i.isIs_finish()!=(Boolean)limits.get("finish")){
                    takes.remove(i);
                    continue;
                }
            }
            if(limits.containsKey("courseID")){
                if(!i.getCourseID().equals(limits.get("courseID"))){
                    takes.remove(i);
                    continue;
                }
            }
//            System.out.println(i.toString());
        }
       return takes;
    }

    /**
     *
     * @param conn
     * @param is_pass 要查的是通过还是未通过
     * @param passNum 通过或未通过的数量
     * @param departmentID
     * @return
     */
    public static ArrayList<Employee> getEmployeeSpec(Connection conn,boolean is_pass,int passNum,String departmentID){
        ArrayList<Take> takes = getTakesInMyDepartment(conn,departmentID);
        HashMap<String,Integer> map =new HashMap<>();// employeeID:count
        for(Take i : takes){
            if(i.isIs_pass()==is_pass){
                if(!map.containsKey(i.getEmployeeID())){
                    map.put(i.getEmployeeID(),1);
                }else{
                    map.put(i.getEmployeeID(),map.get(i.getEmployeeID())+1);
                }
            }
        }
        ArrayList<Employee> result = new ArrayList<>();
        for(String id:map.keySet()){
            if(map.get(id)>=passNum){
                result.add(new Employee(conn,id));
            }
        }
        return result;
    }

    /**
     * 根据id看员工是否修完本部门课程
     */
    public static boolean checkChangeDepartmentStatusById(Connection conn,String employeeID,String departmentID){
        ArrayList<Take> takes = getTakesInMyDepartment(conn, departmentID);

        for(Take i : takes){
            if(!i.getEmployeeID().equals(employeeID)){
                continue;
            }
            //是必修 又 没过 那就不行
            if(!i.isIs_pass() && CourseBelong.checkCourseMandatory(conn,i.getCourseID())){
                return false;
            }
        }
        return true;
    }

    /**
     * 根据姓名
     */
    public static boolean checkChangeDepartmentStatusByName(Connection conn,String departmentID,String employeeName){
        String employeeID = new Employee(conn,employeeName).getEmployeeID();
        return checkChangeDepartmentStatusById(conn,employeeID,departmentID);
    }

    //TODO 根据培训状态是啥意思？不懂

    /**
     * 查询本部门下可以转到其他部门的情况
     * @param conn
     * @param departmentID
     * 这里用employeeBelong暂时地作为键值对表示，实际该对象里是employeename和departmentname
     * @return
     */
    public static ArrayList<EmployeeBelong> checkDepartment(Connection conn,String departmentID) {
        ArrayList<EmployeeBelong> list = new ArrayList<>();
        ArrayList<Employee> employees = getEmployeeMsg(conn,departmentID);//本部门员工列表
        ArrayList<Department> departments = Department.getAllDepartments(conn);//所有部门列表
        //比较每个部门，员工是否可以转，并将匹配成功的转部门结果保存
        for(Department department:departments){
            if(department.getDepartmentID().equals(departmentID)) {
                continue;
            }
            for(Employee employee:employees) {
                if (checkChangeDepartmentStatusById(conn,employee.getEmployeeID(),department.getDepartmentID())){
                    list.add(new EmployeeBelong(employee.getEmployeeName(),department.getName()));
                }
            }
        }
        return list;

    }



        /**
         * 看转入新部门后要培训的课程
         */
    public static ArrayList<Course> getNewCoursesById(Connection conn,String employeeID,String new_departmentID){
        ArrayList<Course> list = getCourseMsg(conn,new_departmentID);
        ArrayList<Take> takes = Take.getTakeByID(conn,employeeID);


        ArrayList<Course> result = new ArrayList<>();

        out:for(Course course : list){
            if(!CourseBelong.checkCourseMandatory(conn,course.courseID)){
                continue;
            }
            for(Take take:takes){
                if(take.isIs_pass() && take.getCourseID().equals(course.courseID)){
                    continue out;
                }
            }
            result.add(course);
        }


        return result;
    }

    public static ArrayList<Course> getNewCoursesByName(Connection conn,String employeeName,String new_departmentID) {
        String employeeID = new Employee(conn,employeeName).getEmployeeID();
        return getNewCoursesById(conn,employeeID,new_departmentID);
    }


}
