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

public class JpaMassAssignmentServlet extends HttpServlet {

    private final JpaUserRepository userRepository = new JpaUserRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String mode = req.getParameter("mode");
        Long id = parseLong(req.getParameter("id"));
        if (id == null) {
            renderBadRequest(req, resp, "用户 id 非法。");
            return;
        }

        EntityManager entityManager = JpaBootstrap.openEntityManager();
        try {
            JpaUserEntity beforeUser = userRepository.findById(entityManager, id);
            if (beforeUser == null) {
                renderBadRequest(req, resp, "目标用户不存在。");
                return;
            }

            entityManager.getTransaction().begin();
            if ("safe".equals(mode)) {
                beforeUser.setEmail(req.getParameter("email"));
                userRepository.save(entityManager, beforeUser);
            } else {
                JpaUserEntity boundUser = new JpaUserEntity();
                boundUser.setId(id);
                boundUser.setUsername(req.getParameter("username"));
                boundUser.setEmail(req.getParameter("email"));
                boundUser.setRole(req.getParameter("role"));
                boundUser.setPasswordHash(req.getParameter("passwordHash"));
                userRepository.save(entityManager, boundUser);
            }
            entityManager.getTransaction().commit();

            JpaUserEntity afterUser = userRepository.findById(entityManager, id);

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<html><body>");
            out.println("<h1>JPA/Hibernate Mass Assignment 示例</h1>");
            out.println("<p>当前模式：<strong>" + ("safe".equals(mode) ? "安全" : "脆弱") + "</strong></p>");
            out.println("<p>关键调用：<code>"
                    + ("safe".equals(mode)
                    ? "只允许更新 email，再 save(entity)"
                    : "@RequestBody / 参数整体绑定实体后直接 save(entity)")
                    + "</code></p>");
            out.println("<h2>更新后实体快照</h2>");
            out.println("<ul>");
            out.println("<li>id=" + afterUser.getId() + "</li>");
            out.println("<li>username=" + HtmlEscaper.escape(afterUser.getUsername()) + "</li>");
            out.println("<li>email=" + HtmlEscaper.escape(afterUser.getEmail()) + "</li>");
            out.println("<li>role=" + HtmlEscaper.escape(afterUser.getRole()) + "</li>");
            out.println("<li>passwordHash=" + HtmlEscaper.escape(afterUser.getPasswordHash()) + "</li>");
            out.println("</ul>");
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
