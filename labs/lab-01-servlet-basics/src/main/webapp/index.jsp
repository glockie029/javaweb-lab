  <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lab 01 Servlet Basics</title>
</head>
<body>
    <h1>Lab 01 - Traditional JavaWeb</h1>
    <p>This page is served by JSP.</p>
    <ul>
        <li><a href="<%= request.getContextPath() %>/hello">Visit HelloServlet</a></li>
        <li><a href="<%= request.getContextPath() %>/login.jsp">Go to Login Page</a></li>
        <li><a href="<%= request.getContextPath() %>/home.jsp">Visit Protected Home</a></li>
    </ul>
</body>
</html>
