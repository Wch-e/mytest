<%@ page import="com.bjposernode.oa.bean.Dept" %>
<%@page contentType="text/html;charset=UTF-8"%>


<!DOCTYPE html>
<html lang='en'>
<head>
    <meta charset='UTF-8'>
    <title>部门详情</title>
</head>
<body>
<h3>欢迎${user.username}</h3>
    <h1>部门详情</h1>
    <hr>
    部门编号：${dept.deptno}<br>
    部门名称：${dept.dname}<br>
    部门位置：${dept.loc}<br>

    <input type='button' value='后退' onclick='window.history.back()'>
</body>
</html>