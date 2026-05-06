<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File IO Demo</title>
</head>
<body>
    <h1>文件输入输出演示</h1>
    <p>当前页面用于观察脆弱与安全实现的差异。</p>

    <h2>下载演示</h2>
    <ul>
        <li><a href="<%= request.getContextPath() %>/download-vuln?file=readme.txt">脆弱下载：正常读取 readme.txt</a></li>
        <li><a href="<%= request.getContextPath() %>/download-vuln?file=../WEB-INF/web.xml">脆弱下载：尝试穿越读取 WEB-INF/web.xml</a></li>
        <li><a href="<%= request.getContextPath() %>/download-safe?file=readme.txt">安全下载：正常读取 readme.txt</a></li>
        <li><a href="<%= request.getContextPath() %>/download-safe?file=../WEB-INF/web.xml">安全下载：尝试穿越读取 WEB-INF/web.xml</a></li>
    </ul>

    <h2>上传演示</h2>
    <p>脆弱上传直接使用原始文件名；安全上传会重命名并限制后缀。</p>

    <h3>脆弱上传</h3>
    <form action="<%= request.getContextPath() %>/upload-vuln" method="post" enctype="multipart/form-data">
        <input type="file" name="file">
        <button type="submit">上传到脆弱接口</button>
    </form>

    <h3>安全上传</h3>
    <form action="<%= request.getContextPath() %>/upload-safe" method="post" enctype="multipart/form-data">
        <input type="file" name="file">
        <button type="submit">上传到安全接口</button>
    </form>

    <p><a href="<%= request.getContextPath() %>/home.jsp">返回首页</a></p>
</body>
</html>
