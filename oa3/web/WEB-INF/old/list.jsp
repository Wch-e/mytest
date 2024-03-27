<%@ page import="java.util.List" %>
<%@ page import="com.bjposernode.oa.bean.Dept" %>
<%@page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang='en'>
<head>
    <meta charset='UTF-8'>
    <title>部门列表</title>
</head>
<body>
    <%--显示登录名--%>
    <h1>欢迎<%=session.getAttribute("username")%></h1>
    <a href="<%=request.getContextPath()%>/user/exit">退出系统</a>

    <h1 align='center'>部门列表</h1>
    <hr>
    <table border='1px' align='center' width='50%'>
        <tr>
            <th>序号</th>
            <th>部门编号</th>
            <th>部门名称</th>
            <th>操作</th>
        </tr>
        <%
            List<Dept> deptList = (List<Dept>)request.getAttribute("deptList");
            int i =0;
            for (Dept dept:deptList){
        %>
            <%--out.write(dept.getDanme());--%>
            <%--<%=dept.getDeptno()%>--%>
        <tr>
            <td><%=++i%></td>
            <td><%=dept.getDeptno()%></td>
            <td><%=dept.getDname()%></td>
            <td>
                <a href='javascript:void(0)' onclick='del(<%=dept.getDeptno()%>)'>删除</a>
                <a href='<%=request.getContextPath()%>/dept/detail?f=edit&dno=<%=dept.getDeptno()%>'>修改</a>
                <a href='<%=request.getContextPath()%>/dept/detail?f=detail&dno=<%=dept.getDeptno()%>'>详情</a>
            </td>
        </tr>
        <%
            }
        %>

    </table>
    <hr>
    <a href='<%=request.getContextPath()%>/add.jsp'>新增部门</a>
</body>
<script type='text/javascript'>
    function del(no) {
        if(window.confirm('亲，删除了不可恢复哦')){
            document.location.href='<%=request.getContextPath()%>/dept/delete?deptno='+no;
        }
    }
</script>
</html>