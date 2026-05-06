<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Protected Home</title>
</head>
<body>
    <h1>Protected Home</h1>
    <p>Current user: <strong><%= session.getAttribute("currentUser") %></strong></p>
    <p>This page is protected by AuthFilter.</p>
    <p>数据越权演示账号：alice / alice123，bob / bob123</p>
    <ul>
        <li><a href="<%= request.getContextPath() %>/hello">Visit HelloServlet</a></li>
        <li><a href="<%= request.getContextPath() %>/order-vuln?id=1001">脆弱示例：查询 alice 自己的订单 1001</a></li>
        <li><a href="<%= request.getContextPath() %>/order-vuln?id=2001">脆弱示例：越权读取 bob 的订单 2001</a></li>
        <li><a href="<%= request.getContextPath() %>/order-safe?id=1001">安全示例：查询 alice 自己的订单 1001</a></li>
        <li><a href="<%= request.getContextPath() %>/order-safe?id=2001">安全示例：尝试读取 bob 的订单 2001</a></li>
        <li><a href="<%= request.getContextPath() %>/file-demo.jsp">文件上传 / 下载 / 路径穿越演示</a></li>
        <li><a href="<%= request.getContextPath() %>/xss-demo.jsp">输出点 / XSS 演示</a></li>
        <li><a href="<%= request.getContextPath() %>/mybatis-demo.jsp">MyBatis #&#123;&#125; / $&#123;&#125; 演示</a></li>
        <li><a href="<%= request.getContextPath() %>/jpa-demo.jsp">JPA / Hibernate 演示</a></li>
        <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
        <li><a href="<%= request.getContextPath() %>/">Back to index</a></li>
    </ul>
</body>
</html>
