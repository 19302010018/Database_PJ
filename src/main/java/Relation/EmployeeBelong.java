package Relation;

import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeBelong {

    String employeeID;
    String departmentID;

    public EmployeeBelong(String employeeID, String departmentID) {
        this.employeeID = employeeID;
        this.departmentID = departmentID;
    }

    public void changeDepartment(Connection conn, EmployeeBelong newBelong){
        String sql = SqlSentence.UPDATE_DEPARTMENT + "'" + newBelong.departmentID + "'" + "where employeeID = " + "'" + newBelong.employeeID + "'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getDepartmentID() {
        return departmentID;
    }
}
