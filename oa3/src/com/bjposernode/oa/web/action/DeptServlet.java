package com.bjposernode.oa.web.action;

import com.bjposernode.oa.bean.Dept;
import com.bjposernode.oa.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({"/dept/list","/dept/detail","/dept/delete","/dept/save","/dept/modify"})
public class DeptServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        //入口的验证（只是获取，没有不新建）
//        HttpSession session = request.getSession(false);
//        if(session != null && session.getAttribute("username") != null){
//            String servletPath = request.getServletPath();
//            if("/dept/list".equals(servletPath)){
//                doList(request,response);
//            }else if ("/dept/detail".equals(servletPath)){
//                doDetail(request,response);
//            }else if("/dept/delete".equals(servletPath)){
//                doDel(request,response);
//            }else if("/dept/save".equals(servletPath)){
//                doSave(request,response);
//            }else if("/dept/modify".equals(servletPath)){
//                doModify(request,response);
//            }
//        }else {
//            //跳转到登录页面
//            response.sendRedirect(request.getContextPath()+"/index.jsp");//访问根，自动知道欢迎页面
//        }
        String servletPath = request.getServletPath();
        if("/dept/list".equals(servletPath)){
            doList(request,response);
        }else if ("/dept/detail".equals(servletPath)){
            doDetail(request,response);
        }else if("/dept/delete".equals(servletPath)){
            doDel(request,response);
        }else if("/dept/save".equals(servletPath)){
            doSave(request,response);
        }else if("/dept/modify".equals(servletPath)){
            doModify(request,response);
        }



    }



    private void doModify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        //解决请求体中文乱码问题
        request.setCharacterEncoding("UTF-8");
        //获取表单数据
        String deptno = request.getParameter("deptno");
        String dname = request.getParameter("dname");
        String loc = request.getParameter("loc");
        //连接数据库执行更新语句
        Connection conn = null;
        PreparedStatement ps =null;
        int count=0;
        try {
            conn = DBUtil.getConnection();
            String sql = "update dept set dname = ?,loc = ? where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,dname);
            ps.setString(2,loc);
            ps.setString(3,deptno);
            count = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(conn,ps,null);
        }
        if(count==1){
            //req.getRequestDispatcher("/dept/list").forward(req,resp);
            response.sendRedirect(request.getContextPath()+"/dept/list");
        }
    }

    private void doSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        //获取部门信息
        //注意乱码问题
        request.setCharacterEncoding("UTF-8");
        String deptno = request.getParameter("deptno");
        String dname = request.getParameter("dname");
        String loc = request.getParameter("loc");
        //连接数据库执行insert语句
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = DBUtil.getConnection();
            String sql = "insert into dept(deptno, dname, loc) values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,deptno);
            ps.setString(2,dname);
            ps.setString(3,loc);
            count = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(conn,ps,null);
        }
        //保存成功跳转到列表页面
        //保存失败跳转到错误页面
        if(count==1){
//            req.getRequestDispatcher("/dept/list").forward(req,resp);
            //这里最好使用重定向
            response.sendRedirect(request.getContextPath()+"/dept/list");
        }
    }

    private void doDel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        //获取部门编号
        String deptno = request.getParameter("deptno");

        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = DBUtil.getConnection();
            String sql = "delete from dept where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,deptno);
            count = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(conn,ps,null);
        }
        if (count==1) {
            //删除成功
            //重定向到列表页面
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath+"/dept/list");
        }

    }

    private void doDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        //封装对象（创建豆子）
        Dept dept = new Dept();
        //获取部门编号
        String dno = request.getParameter("dno");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "select dname,loc from dept where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,dno);
            rs = ps.executeQuery();
            //结果集只有一条，不用循环
            if(rs.next()){
                String dname = rs.getString("dname");
                String loc = rs.getString("loc");

                dept.setDeptno(dno);
                dept.setDname(dname);
                dept.setLoc(loc);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(conn,ps,rs);
        }
        //只有一个豆子，所以不需要袋子，豆子直接放到request域中
        request.setAttribute("dept",dept);

        //转发
        //request.getRequestDispatcher("/detail.jsp").forward(request,response);

        String f = request.getParameter("f");
        if ("edit".equals(f)) {
            //转发到修改页面
            request.getRequestDispatcher("/edit.jsp").forward(request,response);
        }else if ("detail".equals(f)){
            //转发到详情页面
            request.getRequestDispatcher("/detail.jsp").forward(request,response);
        }
        //另一种写法
        //String forward = "/"+request.getParameter("f")+ ".jsp";
        //request.getRequestDispatcher(forward).forward(request,response);
    }

    private void doList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //准备一个容器用来存储部门
        List<Dept> depts = new ArrayList();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "select deptno,dname,loc from dept";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String deptno = rs.getString("deptno");
                String dname = rs.getString("dname");
                String loc = rs.getString("loc");

                //上面零散数据封装成java对象
                Dept dept = new Dept();
                dept.setDeptno(deptno);
                dept.setDname(dname);
                dept.setLoc(loc);

                //将部门对象放到list集合中
                depts.add(dept);


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(conn,ps,rs);
        }

        //将一个集合存放到请求域当中
        request.setAttribute("deptList",depts);
        //转发（不要重定向）
        request.getRequestDispatcher("/list.jsp").forward(request,response);

    }
}
