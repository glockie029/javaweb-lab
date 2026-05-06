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

public class JpaAccessServlet extends HttpServlet {

    private final JpaOrderRepository orderRepository = new JpaOrderRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String mode = req.getParameter("mode");
        Long id = parseLong(req.getParameter("id"));
        String currentUser = (String) req.getSession(false).getAttribute("currentUser");

        if (id == null) {
            renderBadRequest(req, resp, "订单 id 非法。");
            return;
        }

        EntityManager entityManager = JpaBootstrap.openEntityManager();
        try {
            JpaOrderEntity order;
            if ("safe".equals(mode)) {
                order = orderRepository.findByIdAndOwner(entityManager, id, currentUser);
            } else {
                order = orderRepository.findById(entityManager, id);
            }

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<html><body>");
            out.println("<h1>JPA/Hibernate 资源归属校验示例</h1>");
            out.println("<p>当前模式：<strong>" + ("safe".equals(mode) ? "安全" : "脆弱") + "</strong></p>");
            out.println("<p>当前用户：<code>" + HtmlEscaper.escape(currentUser) + "</code></p>");
            out.println("<p>Repository 调用：<code>"
                    + ("safe".equals(mode)
                    ? "findByIdAndOwner(id, currentUser)"
                    : "findById(id)")
                    + "</code></p>");
            if (order == null) {
                out.println("<p style=\"color:red;\">未找到订单，或当前用户无权访问该订单。</p>");
            } else {
                out.println("<ul>");
                out.println("<li>id=" + order.getId() + "</li>");
                out.println("<li>owner=" + HtmlEscaper.escape(order.getOwner()) + "</li>");
                out.println("<li>itemName=" + HtmlEscaper.escape(order.getItemName()) + "</li>");
                out.println("<li>status=" + HtmlEscaper.escape(order.getStatus()) + "</li>");
                out.println("</ul>");
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
