package Util;

import java.sql.Connection;

public class TeacherTranslator extends ArgTranslator{

    public TeacherTranslator(String employeeID, Connection conn) {
        super(employeeID,conn);
    }
}
