package com.example.lab01.servlet;

import com.example.lab01.model.MyBatisUserRecord;
import com.example.lab01.util.MyBatisBootstrap;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyBatisVulnerableServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        List<MyBatisUserRecord> users = MyBatisBootstrap.selectByUsernameVulnerable(username);

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>MyBatis 脆弱示例：使用 ${}</h1>");
        out.println("<p>输入参数 username：<code>" + escapeHtml(username) + "</code></p>");
        out.println("<p>Mapper XML SQL：<code>where username = '${username}'</code></p>");
        out.println("<p style=\"color:red;\">${} 会先做字符串替换，再送给数据库执行。</p>");
        renderUsers(out, users);
        out.println("<p><a href=\"" + req.getContextPath() + "/mybatis-demo.jsp\">返回 MyBatis 演示页</a></p>");
        out.println("</body></html>");
    }

    private void renderUsers(PrintWriter out, List<MyBatisUserRecord> users) {
        out.println("<p>查询结果数量：" + users.size() + "</p>");
        out.println("<ul>");
        for (MyBatisUserRecord user : users) {
            out.println("<li>id=" + user.getId() + ", username=" + escapeHtml(user.getUsername())
                    + ", role=" + escapeHtml(user.getRole()) + "</li>");
        }
        out.println("</ul>");
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        String escaped = input;
        escaped = escaped.replace("&", "&amp;");
        escaped = escaped.replace("<", "&lt;");
        escaped = escaped.replace(">", "&gt;");
        escaped = escaped.replace("\"", "&quot;");
        escaped = escaped.replace("'", "&#39;");
        return escaped;
    }
}
