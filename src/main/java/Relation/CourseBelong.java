package Relation;

import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseBelong {
    public static Boolean checkCourseMandatory(Connection conn, String courseID) {
        String mandatory = "";
        System.out.println("正在检查课程" + courseID + "是否为必修课");
        String sql = SqlSentence.CHECK_COURSE_MANDATORY + "'" + courseID + "'";
//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                mandatory = rs.getString("mandatory").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return !mandatory.equals("0");//0不是必修课，1是必修课

    }
}
