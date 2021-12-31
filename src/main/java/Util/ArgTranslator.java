package Util;

import java.sql.Connection;

public class ArgTranslator {


    String employeeID;
    Connection conn;

    ArgTranslator(String employeeID,Connection conn){
        this.employeeID = employeeID;
        this.conn = conn;

    }


    public void help(){

    }
    public void translate(String cmd){
        System.out.println("arg cmd");
    }


}
