package Util;

import java.sql.Connection;

public class TeacherAndManagerTranslator extends ArgTranslator{
    public TeacherAndManagerTranslator(String employeeID, Connection conn) {
        super(employeeID,conn);
    }
}
