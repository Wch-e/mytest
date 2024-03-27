package com.bjposernode.oa.web.action;

import com.bjposernode.oa.bean.User;
import com.bjposernode.oa.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取cookie
        Cookie[] cookies = request.getCookies();
        String username = null;
        String password = null;
        if (cookies != null){
            for (Cookie cookie : cookies){
                String name = cookie.getName();
                if ("username".equals(name)){
                    username = cookie.getValue();
                }else if ("password".equals(name)){
                    password = cookie.getValue();
                }
            }
        }

        if(username != null && password != null){
            //验证用户名和密码是否正确
            //正确，表示登录成功
            //错误，表示登录失败
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean sucess = false;
            try {
                conn = DBUtil.getConnection();
                String sql = "select * from user where username = ? and password = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1,username);
                ps.setString(2,password);
                rs = ps.executeQuery();
                if (rs.next()) {
                    //登录成功
                    sucess = true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                DBUtil.close(conn,ps,rs);
            }

            if(sucess){
                //获取session
                HttpSession session = request.getSession();
                //session.setAttribute("username",username);
                User user = new User(username,password);
                session.setAttribute("user",user);

                //登录成功
                response.sendRedirect(request.getContextPath()+"/dept/list");
            }else {
                //登录失败
                response.sendRedirect(request.getContextPath()+"/index.jsp");
            }

        }else {
            //跳到登录页面
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        }

    }
}
