package com.bjposernode.oa.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        /*
        * 什么情况下不能拦截？
        *
        *   用户访问index.jsp
        *   用户已经登录了
        *   用户要去登录
        *   WelcomeServlet不能拦截
        *
        * */

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取请求路径
        String servletPath = request.getServletPath();

        //入口的验证（只是获取，没有不新建）
        HttpSession session = request.getSession(false);
        if("/index.jsp".equals(servletPath) || "/welcome".equals(servletPath) ||
                "/user/login".equals(servletPath) || "/user/exit".equals(servletPath) ||
                (session != null && session.getAttribute("user") != null)){
            //继续往下走
            chain.doFilter(request,response);
        }else {
            //跳转到登录页面
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
