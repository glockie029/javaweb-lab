package com.example.lab01.servlet;

import com.example.lab01.entity.JpaOrderEntity;
import com.example.lab01.repository.JpaOrderRepository;
import com.example.lab01.util.HtmlEscaper;
import com.example.lab01.util.JpaBootstrap;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JpaUpdateServlet extends HttpServlet {

    private final JpaOrderRepository orderRepository = new JpaOrderRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String mode = req.getParameter("mode");
        Long id = parseLong(req.getParameter("id"));
        String status = req.getParameter("status");
        String currentUser = (String) req.getSession(false).getAttribute("currentUser");

        if (id == null || status == null || status.trim().isEmpty()) {
            renderBadRequest(req, resp, "订单 id 或 status 非法。");
            return;
        }

        EntityManager entityManager = JpaBootstrap.openEntityManager();
        try {
            JpaOrderEntity beforeOrder = orderRepository.findById(entityManager, id);
            entityManager.getTransaction().begin();
            int affectedRows;
            if ("safe".equals(mode)) {
                affectedRows = orderRepository.updateStatusByIdAndOwner(entityManager, id, status, currentUser);
            } else {
                affectedRows = orderRepository.updateStatusById(entityManager, id, status);
            }
            entityManager.getTransaction().commit();
            JpaOrderEntity afterOrder = orderRepository.findById(entityManager, id);

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<html><body>");
            out.println("<h1>JPA/Hibernate 批量更新 / 越权更新示例</h1>");
            out.println("<p>当前模式：<strong>" + ("safe".equals(mode) ? "安全" : "脆弱") + "</strong></p>");
            out.println("<p>当前用户：<code>" + HtmlEscaper.escape(currentUser) + "</code></p>");
            out.println("<p>更新前状态：<code>" + renderStatus(beforeOrder) + "</code></p>");
            out.println("<p>影响行数：<code>" + affectedRows + "</code></p>");
            out.println("<p>更新后状态：<code>" + renderStatus(afterOrder) + "</code></p>");
            out.println("<p>关键调用：<code>"
                    + ("safe".equals(mode)
                    ? "update ... where id = :id and owner = :owner"
                    : "update ... where id = :id")
                    + "</code></p>");
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

    private String renderStatus(JpaOrderEntity order) {
        if (order == null) {
            return "order not found";
        }
        return "id=" + order.getId()
                + ", owner=" + HtmlEscaper.escape(order.getOwner())
                + ", status=" + HtmlEscaper.escape(order.getStatus());
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
