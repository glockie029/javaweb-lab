package com.example.lab01.servlet;

import com.example.lab01.entity.JpaUserEntity;
import com.example.lab01.repository.JpaUserRepository;
import com.example.lab01.util.HtmlEscaper;
import com.example.lab01.util.JpaBootstrap;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JpaExposureServlet extends HttpServlet {

    private final JpaUserRepository userRepository = new JpaUserRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = parseLong(req.getParameter("id"));
        if (id == null) {
            renderBadRequest(req, resp, "用户 id 非法。");
            return;
        }

        EntityManager entityManager = JpaBootstrap.openEntityManager();
        try {
            JpaUserEntity user = userRepository.findById(entityManager, id);

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<html><body>");
            out.println("<h1>JPA/Hibernate 实体直接暴露示例</h1>");
            out.println("<p>Repository 调用：<code>findById(id)</code></p>");
            if (user == null) {
                out.println("<p style=\"color:red;\">用户不存在。</p>");
            } else {
                out.println("<p style=\"color:red;\">直接返回实体时，敏感字段也可能被序列化或输出。</p>");
                out.println("<pre>{");
                out.println("  \"id\": " + user.getId() + ",");
                out.println("  \"username\": \"" + HtmlEscaper.escape(user.getUsername()) + "\",");
                out.println("  \"email\": \"" + HtmlEscaper.escape(user.getEmail()) + "\",");
                out.println("  \"role\": \"" + HtmlEscaper.escape(user.getRole()) + "\",");
                out.println("  \"passwordHash\": \"" + HtmlEscaper.escape(user.getPasswordHash()) + "\"");
                out.println("}</pre>");
            }
            out.println("<p><a href=\"" + req.getContextPath() + "/jpa-demo.jsp\">返回 JPA/Hibernate 演示页</a></p>");
            out.println("</body></html>");
        } finally {
            entityManager.close();
        }
    }

    private Long parseLong(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(rawValue.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void renderBadRequest(HttpServletRequest req, HttpServletResponse resp, String message)
            throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>JPA/Hibernate 参数错误</h1>");
        out.println("<p style=\"color:red;\">" + HtmlEscaper.escape(message) + "</p>");
        out.println("<p><a href=\"" + req.getContextPath() + "/jpa-demo.jsp\">返回 JPA/Hibernate 演示页</a></p>");
        out.println("</body></html>");
    }
}
