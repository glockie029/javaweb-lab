<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <p>Demo account: admin / admin123</p>

    <% if ("1".equals(request.getParameter("error"))) { %>
        <p style="color: red;">Invalid username or password.</p>
    <% } %>

    <form action="<%= request.getContextPath() %>/login" method="post">
        <p>
            <label>Username:
                <input type="text" name="username">
            </label>
        </p>
        <p>
            <label>Password:
                <input type="password" name="password">
            </label>
        </p>
        <button type="submit">Login</button>
    </form>

    <p><a href="<%= request.getContextPath() %>/">Back to index</a></p>
</body>
</html>
