package com.example.lab01.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VulnerableXssServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String comment = req.getParameter("comment");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>脆弱输出示例</h1>");
        out.println("<p>原始输入：</p>");
        out.println("<div style=\"border:1px solid #999;padding:12px;\">" + comment + "</div>");
        out.println("<p style=\"color:red;\">问题：comment 直接进入 HTML 输出，浏览器会把它当成页面代码的一部分。</p>");
        out.println("<p><a href=\"" + req.getContextPath() + "/xss-demo.jsp\">返回 XSS 演示页</a></p>");
        out.println("</body></html>");
    }
}
