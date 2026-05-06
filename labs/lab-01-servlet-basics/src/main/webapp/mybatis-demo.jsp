<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MyBatis Demo</title>
</head>
<body>
    <h1>MyBatis #&#123;&#125; 与 $&#123;&#125; 演示</h1>
    <p>推荐测试输入：</p>
    <ul>
        <li>正常输入：alice</li>
        <li>注入输入：alice' OR '1'='1</li>
    </ul>

    <h2>脆弱查询：$&#123;&#125;</h2>
    <form action="<%= request.getContextPath() %>/mybatis-vuln" method="get">
        <input type="text" name="username" style="width: 420px;">
        <button type="submit">提交到脆弱接口</button>
    </form>

    <h2>安全查询：#&#123;&#125;</h2>
    <form action="<%= request.getContextPath() %>/mybatis-safe" method="get">
        <input type="text" name="username" style="width: 420px;">
        <button type="submit">提交到安全接口</button>
    </form>

    <p><a href="<%= request.getContextPath() %>/home.jsp">返回首页</a></p>
</body>
</html>
