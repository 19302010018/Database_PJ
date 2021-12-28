package Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class SqlSentence {
    public static final String EMPLOYEE_MSG_EVERY = "select * from employee natural join employeeBelong ";
    public static final String COURSE_MSG_EVERY = "select * from course natural join courseBelong ";
    public static final String GET_COURSES_BY_TEACHER_ID = "select courseID from teach where teacherID=";
    public static final String GET_EMPLOYEE_ID_BY_COURSE_ID = "select employeeID from take where courseID=";
    public static final String GET_DEPARTMENT_ID_BY_EMPLOYEE_ID = "select departmentID from employeeBelong where employeeID=";
    public static final String GET_DEPARTMENT_ID_BY_COURSE_ID = "select departmentID from courseBelong where courseID=";
    public static final String CHECK_COURSE_MANDATORY = "select mandatory from courseBelong where courseID= ";
    public static final String GET_MANDATORY_COURSES = "select * from courseBelong where mandatory=1";
    public static final String GET_EMPLOYEE_ID_BY_DEPARTMENT_ID = "select employeeID from employeeBelong where departmentID = ";
    public static final String GET_EXAM = "select * from take ";
    public static final String GET_TEACHER_ID_BY_COURSE_ID = "select * from teach ";


    public static final String UPDATE_EMPLOYEE_MSG = "update employee set ";
    public static final String UPDATE_COURSE_MSG = "update course set ";
    public static final String UPDATE_GRADE = "update take set ";

    public static final String ASSIGN_COURSE = "insert into take (employeeID,courseID,finished) values ";
    public static final String ADD_EMPLOYEE = "insert into employee values ";
    public static final String ADD_COURSE = "insert into course values ";
    public static final String ADD_EMPLOYEE_BELONG = "insert into employeeBelong values ";
    public static final String ADD_COURSE_BELONG = "insert into courseBelong values ";
    public static final String ADD_TEACH = "insert into teach values ";


    public static final String SPECIFY_BY_DEPARTMENT_ID = "  departmentID='";
    public static final String SPECIFY_BY_EMPLOYEE_ID = "  employeeID='";
    public static final String SPECIFY_BY_EMPLOYEE_NAME = "  employeeName='";
    public static final String SPECIFY_BY_EMPLOYEE_TELEPHONE = "  telephone='";
    public static final String SPECIFY_BY_EMPLOYEE_EMAIL = "  email='";
    public static final String SPECIFY_BY_EMPLOYEE_ADDRESS = "  address='";
    public static final String SPECIFY_BY_EMPLOYEE_AGE = "  age= ";
    public static final String SPECIFY_BY_COURSE_ID = "  courseID= '";
    public static final String SPECIFY_BY_GRADE = "  grade= ";
    public static final String SPECIFY_BY_FINISHED = "  finished= ";
    public static final String SPECIFY_BY_STATUS = "  status= ";
    public static final String SPECIFY_BY_TEACHER_ID = "  teacherID= '";
    public static final String SPECIFY_BY_MANDATORY = "  mandatory= ";
    public static final String SPECIFY_BY_NAME = "  name= '";
    public static final String SPECIFY_BY_TYPE = "  type= '";
    public static final String SPECIFY_BY_SYLLABUS = "  syllabus= '";
    public static final String SPECIFY_BY_SEX = "  sex= '";
    public static final String SPECIFY_BY_ENTRY_TIME = "  entryTime= '";


    public static final String WHERE_KEY = " where ";
    public static final String AND_KEY = " and ";
    public static final String COMMA_KEY = " , ";
    public static final String LEFT_PARENTHESES_KEY = " ( ";
    public static final String RIGHT_PARENTHESES_KEY = " ) ";
    public static final String STRING_POSTFIX = "'";

    public static final String GET_ID_BY_EMPLOYEE_NAME = "select employeeID from employee  where employeeName=  ";
    public static final String GET_NAME_BY_EMPLOYEE_ID = "select employeeName from employee  where employeeID=  ";
    public static final String GET_TEACH_TIME_BY_ID = "select * from teacher where employeeID=";
    public static final String CHECK_IS_TEACHER = "select * from teacher";
    public static final String CHECK_IS_MANAGER = "select * from manager";

    public static final String DELETE_EMPLOYEE="delete from employee where employeeID=";
    public static final String DELETE_EMPLOYEE_BELONG="delete from employeeBelong where employeeID=";
    public static final String DELETE_TAKE_BY_EMPLOYEE_ID="delete from take where employeeID=";
    public static final String DELETE_TEACHER="delete from teacher where employeeID=";
    public static final String DELETE_TEACH="delete from teach where teacherID=";
    public static final String DELETE_TEACH_BY_COURSE="delete from teach where courseID=";
    public static final String DELETE_COURSE="delete from course where courseID=";
    public static final String DELETE_COURSE_BELONG="delete from courseBelong where courseID=";
    public static final String DELETE_TAKE_BY_COURSE_ID="delete from take where courseID=";
    public static final String DELETE_MANAGER="delete from manager where employeeID=";


    public static String whereClauseGenerator(HashMap limits) {
        return WHERE_KEY + sqlGenerator(limits, AND_KEY);
    }

    public static String updateClauseGenerator(HashMap limits) {
        return sqlGenerator(limits, COMMA_KEY);
    }


    public static String insertClauseGenerator(LinkedHashMap msgs) {
        StringBuilder sql = new StringBuilder();
        sql.append(LEFT_PARENTHESES_KEY);
        Iterator iterator = msgs.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (msgs.get(key).getClass().toString().contains("String")) {
                sql.append(STRING_POSTFIX).append(msgs.get(key)).append(STRING_POSTFIX);
            } else {
                sql.append(msgs.get(key));
            }
            if (iterator.hasNext()) {
                sql.append(COMMA_KEY);
            }
        }
        sql.append(RIGHT_PARENTHESES_KEY);
//        System.out.println(sql);
        return sql.toString();
    }

    public static String sqlGenerator(HashMap limits, String seperator) {
        StringBuilder sql = new StringBuilder();
        Iterator iterator = limits.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            switch (key) {
                case "departmentID":
                    sql.append(SPECIFY_BY_DEPARTMENT_ID).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "employeeID":
                    sql.append(SPECIFY_BY_EMPLOYEE_ID).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "employeeName":
                    sql.append(SPECIFY_BY_EMPLOYEE_NAME).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "age":
                    sql.append(SPECIFY_BY_EMPLOYEE_AGE).append(limits.get(key));
                    break;
                case "address":
                    sql.append(SPECIFY_BY_EMPLOYEE_ADDRESS).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "telephone":
                    sql.append(SPECIFY_BY_EMPLOYEE_TELEPHONE).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "email":
                    sql.append(SPECIFY_BY_EMPLOYEE_EMAIL).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "courseID":
                    sql.append(SPECIFY_BY_COURSE_ID).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "grade":
                    sql.append(SPECIFY_BY_GRADE).append(limits.get(key));
                    break;
                case "finished":
                    sql.append(SPECIFY_BY_FINISHED).append(limits.get(key));
                    break;
                case "status":
                    sql.append(SPECIFY_BY_STATUS).append(limits.get(key));
                    break;
                case "teacherID":
                    sql.append(SPECIFY_BY_TEACHER_ID).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "mandatory":
                    sql.append(SPECIFY_BY_MANDATORY).append(limits.get(key));
                    break;
                case "name":
                    sql.append(SPECIFY_BY_NAME).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "type":
                    sql.append(SPECIFY_BY_TYPE).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "syllabus":
                    sql.append(SPECIFY_BY_SYLLABUS).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "sex":
                    sql.append(SPECIFY_BY_SEX).append(limits.get(key)).append(STRING_POSTFIX);
                    break;
                case "entryTime":
                    sql.append(SPECIFY_BY_ENTRY_TIME).append(limits.get(key)).append(STRING_POSTFIX);
                    break;


                default:
                    break;
            }
            if (iterator.hasNext()) {
                sql.append(seperator);
            }


        }
        return sql.toString();
    }


}
;