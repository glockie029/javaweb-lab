<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>XSS Demo</title>
</head>
<body>
    <h1>XSS 输出点演示</h1>
    <p>可测试输入示例：&lt;script&gt;alert(1)&lt;/script&gt;</p>
    <p>也可测试：&lt;img src=x onerror=alert(1)&gt;</p>

    <h2>脆弱输出</h2>
    <form action="<%= request.getContextPath() %>/xss-vuln" method="get">
        <input type="text" name="comment" style="width: 420px;">
        <button type="submit">提交到脆弱接口</button>
    </form>

    <h2>安全输出</h2>
    <form action="<%= request.getContextPath() %>/xss-safe" method="get">
        <input type="text" name="comment" style="width: 420px;">
        <button type="submit">提交到安全接口</button>
    </form>

    <p><a href="<%= request.getContextPath() %>/home.jsp">返回首页</a></p>
</body>
</html>
