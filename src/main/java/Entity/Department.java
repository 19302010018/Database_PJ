package Entity;

import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Department {
    String departmentID;
    String name;

    Department(String departmentID,String name){
        this.departmentID = departmentID;
        this.name = name;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentID='" + departmentID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static ArrayList<Department> getAllDepartments(Connection conn){
        String sql = "select * from department";
        PreparedStatement pstmt;
        ArrayList<Department> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String departmentID = rs.getString("departmentID").trim();
                String name = rs.getString("name").trim();
                list.add(new Department(departmentID,name));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
}
