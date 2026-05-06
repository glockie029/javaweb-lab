package com.example.lab01.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SafeXssServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String comment = req.getParameter("comment");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>安全输出示例</h1>");
        out.println("<p>经过 HTML 转义后的输入：</p>");
        out.println("<div style=\"border:1px solid #999;padding:12px;\">" + escapeHtml(comment) + "</div>");
        out.println("<p style=\"color:green;\">这里先做了 HTML 转义，因此输入只会被当成普通文本显示。</p>");
        out.println("<p><a href=\"" + req.getContextPath() + "/xss-demo.jsp\">返回 XSS 演示页</a></p>");
        out.println("</body></html>");
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
