import java.sql.*;

import org.apache.ibatis.jdbc.ScriptRunner;

public class main {
    private static final String username = "root";
    private static final String password = "12345Aa?";
    private static final String url ="jdbc:mysql://47.101.188.143:3306/dbpj";

    public static void main(String[] args) {


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("注册驱动成功");
        } catch (ClassNotFoundException e) {
            System.out.println("注册驱动失败");
            e.printStackTrace();
            return ;
        }
        Connection conn=null;

        try {
            conn = DriverManager.getConnection(url, username, password);
            ScriptRunner runner = new ScriptRunner(conn);

        }catch(Exception e){
            System.out.println("wrong");
        }

    }

}


