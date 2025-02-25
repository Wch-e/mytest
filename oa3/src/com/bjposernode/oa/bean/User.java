package com.bjposernode.oa.bean;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Objects;

public class User implements HttpSessionBindingListener {
    private String username;
    private String password;

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        //用户登录了
        //User类型的对象向session存放了
        //获取ServletContext对象
        ServletContext application = event.getSession().getServletContext();
        //获取在线人数
        Object onlinecount = application.getAttribute("onlinecount");
        if(onlinecount == null){
            application.setAttribute("onlinecount",1);
        }else {
            int count = (Integer)onlinecount;
            count++;
            application.setAttribute("onlinecount",count);
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        //用户退出了
        //User类型的对象从session域中删除了
        ServletContext application = event.getSession().getServletContext();
        Integer onlinecount = (Integer) application.getAttribute("onlinecount");
        onlinecount--;
        application.setAttribute("onlinecount",onlinecount);
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }


}
