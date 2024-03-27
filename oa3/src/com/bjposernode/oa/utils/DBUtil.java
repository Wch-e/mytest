package com.bjposernode.oa.utils;

import java.sql.*;
import java.util.ResourceBundle;

/*
*JDBC工具类
 */
public class DBUtil {

    //静态变量,类加载时执行
    //自上而下执行
    //属性资源文件绑定
    private static ResourceBundle bundle = ResourceBundle.getBundle("resources.jdbc");
    private static String driver = bundle.getString("driver");
    private static String url = bundle.getString("url");
    private static String user = bundle.getString("user");
    private static String password = bundle.getString("password");

    static {
        //注册驱动
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
    *获取数据库连接对象
    * @return conn 连接对象
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
        //获取连接
        Connection conn = DriverManager.getConnection(url,user,password);
        return conn;
    }

    /**
     * 释放资源
     * conn 连接对象
     * ps 数据库操作对象
     * rs 结果集对象
     */
    public static void close(Connection conn, Statement ps, ResultSet rs){
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (ps != null){
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }


}
