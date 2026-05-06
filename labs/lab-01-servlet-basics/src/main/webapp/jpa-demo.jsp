<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>JPA / Hibernate Demo</title>
</head>
<body>
    <h1>JPA / Hibernate 最小演示</h1>
    <p>当前覆盖点：</p>
    <ul>
        <li>Entity / Repository / EntityManager</li>
        <li>资源归属校验缺失导致越权</li>
        <li>JPQL/HQL 注入</li>
        <li>原生 SQL 注入</li>
        <li>批量更新 / 越权更新</li>
        <li>Mass Assignment</li>
        <li>实体直接暴露导致敏感数据输出</li>
    </ul>

    <h2>1. 资源归属校验</h2>
    <ul>
        <li><a href="<%= request.getContextPath() %>/jpa-access?mode=vuln&id=1001">脆弱示例：alice 读取自己的订单 1001</a></li>
        <li><a href="<%= request.getContextPath() %>/jpa-access?mode=vuln&id=2001">脆弱示例：alice 越权读取 bob 的订单 2001</a></li>
        <li><a href="<%= request.getContextPath() %>/jpa-access?mode=safe&id=1001">安全示例：读取自己的订单 1001</a></li>
        <li><a href="<%= request.getContextPath() %>/jpa-access?mode=safe&id=2001">安全示例：尝试读取 bob 的订单 2001</a></li>
    </ul>

    <h2>2. JPQL/HQL 注入</h2>
    <p>推荐测试输入：<code>alice' OR '1'='1</code></p>
    <form action="<%= request.getContextPath() %>/jpa-query" method="get">
        <input type="hidden" name="queryType" value="jpql">
        <input type="hidden" name="mode" value="vuln">
        <input type="text" name="owner" style="width: 420px;">
        <button type="submit">提交到 JPQL 脆弱接口</button>
    </form>
    <form action="<%= request.getContextPath() %>/jpa-query" method="get">
        <input type="hidden" name="queryType" value="jpql">
        <input type="hidden" name="mode" value="safe">
        <input type="text" name="owner" style="width: 420px;">
        <button type="submit">提交到 JPQL 安全接口</button>
    </form>

    <h2>3. 原生 SQL 注入</h2>
    <p>推荐测试输入：<code>alice' OR '1'='1</code></p>
    <form action="<%= request.getContextPath() %>/jpa-query" method="get">
        <input type="hidden" name="queryType" value="native">
        <input type="hidden" name="mode" value="vuln">
        <input type="text" name="owner" style="width: 420px;">
        <button type="submit">提交到 Native Query 脆弱接口</button>
    </form>
    <form action="<%= request.getContextPath() %>/jpa-query" method="get">
        <input type="hidden" name="queryType" value="native">
        <input type="hidden" name="mode" value="safe">
        <input type="text" name="owner" style="width: 420px;">
        <button type="submit">提交到 Native Query 安全接口</button>
    </form>

    <h2>4. 批量更新 / 越权更新</h2>
    <ul>
        <li><a href="<%= request.getContextPath() %>/jpa-update?mode=vuln&id=2001&status=CANCELLED">脆弱示例：alice 越权修改 bob 的订单 2001 状态</a></li>
        <li><a href="<%= request.getContextPath() %>/jpa-update?mode=safe&id=2001&status=PAID">安全示例：alice 尝试修改 bob 的订单 2001 状态</a></li>
    </ul>

    <h2>5. Mass Assignment</h2>
    <p>目标用户固定为 alice(id=1)。脆弱模式会直接保存整个实体，安全模式只更新 email。</p>
    <form action="<%= request.getContextPath() %>/jpa-mass-update" method="post">
        <input type="hidden" name="mode" value="vuln">
        <input type="hidden" name="id" value="1">
        <p>username: <input type="text" name="username" value="alice"></p>
        <p>email: <input type="text" name="email" value="alice-updated@example.com"></p>
        <p>role: <input type="text" name="role" value="admin"></p>
        <p>passwordHash: <input type="text" name="passwordHash" value="changed-by-client"></p>
        <button type="submit">提交到脆弱 Mass Assignment 接口</button>
    </form>
    <form action="<%= request.getContextPath() %>/jpa-mass-update" method="post">
        <input type="hidden" name="mode" value="safe">
        <input type="hidden" name="id" value="1">
        <input type="hidden" name="username" value="alice">
        <input type="hidden" name="role" value="admin">
        <input type="hidden" name="passwordHash" value="changed-by-client">
        <p>email: <input type="text" name="email" value="alice-safe@example.com"></p>
        <button type="submit">提交到安全 Mass Assignment 接口</button>
    </form>

    <h2>6. 实体直接暴露</h2>
    <ul>
        <li><a href="<%= request.getContextPath() %>/jpa-exposure?id=1">直接暴露 alice 实体</a></li>
        <li><a href="<%= request.getContextPath() %>/jpa-exposure?id=3">直接暴露 admin 实体</a></li>
    </ul>

    <p><a href="<%= request.getContextPath() %>/home.jsp">返回首页</a></p>
</body>
</html>
