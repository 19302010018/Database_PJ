package Util;

import java.sql.Connection;

public class ManagerTranslator extends ArgTranslator{
    public ManagerTranslator(String employeeID, Connection conn) {
        super(employeeID,conn);
    }
}
