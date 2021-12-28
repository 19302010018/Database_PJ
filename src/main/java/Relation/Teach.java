package Relation;

import Util.SqlSentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Teach {
    public static String getTeacherIDByCourseID(Connection conn, String courseID) {
        String teacherID = "";
        System.out.println("正在查找" + courseID + "的任课教师");
        HashMap limits = new HashMap();
        limits.put("courseID", courseID);

        String sql = SqlSentence.GET_TEACHER_ID_BY_COURSE_ID + SqlSentence.whereClauseGenerator(limits);

//        System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                teacherID = rs.getString("teacherID").trim();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teacherID;
    }

}
