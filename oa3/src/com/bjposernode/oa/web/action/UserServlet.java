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

@WebServlet({"/user/login","/user/exit"})
public class UserServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/user/login".equals(servletPath)){
            doLogin(request,response);
        }else if("/user/exit".equals(servletPath)){
            doExit(request,response);
        }
    }

    protected void doExit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取session对象，销毁session
        HttpSession session = request.getSession(false);
        if (session != null) {

            //从session域中删除user对象
            session.removeAttribute("user");

            //手动销毁session对象
            session.invalidate();
            //销毁cookie（退出系统将所有的cookie全部销毁）
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    //设置cookie的有效期为0，表示删除该cookie
                    cookie.setMaxAge(0);
                    //设置下一个cookie的路径
                    cookie.setPath(request.getContextPath());//删除cookie的时候注意路径问题
                    //响应给浏览器，浏览器会将之前的cookie覆盖
                    response.addCookie(cookie);
                }
            }
            //跳转到登录页面
            response.sendRedirect(request.getContextPath());
        }
    }
    protected void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean success = false;

        //获取表单数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from user where username = ? and password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
            //这个结果集最多只有一条数据
            if (rs.next()) {
                //登录成功
                success = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(conn,ps,rs);
        }

        if (success){
            //获取session对象（这里必须获取到session，没有也要新建）
            HttpSession session = request.getSession();
            //session.setAttribute("username",username);
            User user = new User(username,password);
            session.setAttribute("user",user);

            //登录成功了，并且用户确实选择了“十天免登录功能”
            String f = request.getParameter("f");
            if ("1".equals(f)) {
                //创建cookie对象，存储登录名
                Cookie cookie1 = new Cookie("username", username);
                //创建cookie对象，存储登录密码
                Cookie cookie2 = new Cookie("password",password);//真实情况下是加密的
                //设置cookie的有效期为十天
                cookie1.setMaxAge(60*60*24*10);
                cookie2.setMaxAge(60*60*24*10);
                //设置cookie的path（只要访问这个应用，浏览器就一定要携带这两个cookie）
                cookie1.setPath(request.getContextPath());
                cookie2.setPath(request.getContextPath());
                //响应cookie给浏览器
                response.addCookie(cookie1);
                response.addCookie(cookie2);
            }
            //成功
            response.sendRedirect(request.getContextPath()+"/dept/list");
        }else {
            //失败
            response.sendRedirect(request.getContextPath()+"/error.jsp");
        }
    }


}