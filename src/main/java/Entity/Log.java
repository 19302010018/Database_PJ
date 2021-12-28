package Entity;

import Util.SqlSentence;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class Log {
    int id;
    String username;
    String operation;
    Date date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    Log(){

    }
    /**
     *
     * @param conn
     * @param username username
     * @param operation 具体的operation
     * 在每个需要log的方法处new一个Log即可
     */
    public Log(Connection conn, String username, String operation){
        LinkedHashMap msgs = new LinkedHashMap();
        msgs.put("username", username);
        msgs.put("operation", operation);
        Date date = new Date();//获得系统时间.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String nowTime = sdf.format(date);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp dates =Timestamp.valueOf(nowTime);//把时间转换
        msgs.put("time",dates);
        String sql = SqlSentence.INSERT_LOG + SqlSentence.insertClauseGenerator(msgs);
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 获取所有Log
     * @param conn
     * @return
     */
    public static List<Log> getLogs(Connection conn){
        String sql = "select * from log";
        PreparedStatement pstmt;
        ArrayList<Log> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Log log = new Log();
                log.setId(rs.getInt("id"));
                log.setDate(rs.getDate("time"));
                log.setUsername(rs.getString("username").trim());
                log.setOperation(rs.getString("operation").trim());
                list.add(log);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    /**
     * delete log
     * @param conn
     * @param id
     */
    public static void deleteLog(Connection conn,int id){
        String sql = "delete from log where id = " + id;
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
