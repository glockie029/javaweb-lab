package com.example.lab01.servlet;

import com.example.lab01.entity.JpaOrderEntity;
import com.example.lab01.repository.JpaOrderRepository;
import com.example.lab01.util.HtmlEscaper;
import com.example.lab01.util.JpaBootstrap;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JpaQueryServlet extends HttpServlet {

    private final JpaOrderRepository orderRepository = new JpaOrderRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String queryType = req.getParameter("queryType");
        String mode = req.getParameter("mode");
        String owner = req.getParameter("owner");

        EntityManager entityManager = JpaBootstrap.openEntityManager();
        try {
            List<JpaOrderEntity> orders;
            if ("native".equals(queryType)) {
                orders = "safe".equals(mode)
                        ? orderRepository.findByOwnerNativeSafe(entityManager, owner)
                        : orderRepository.findByOwnerNativeVulnerable(entityManager, owner);
            } else {
                orders = "safe".equals(mode)
                        ? orderRepository.findByOwnerJpqlSafe(entityManager, owner)
                        : orderRepository.findByOwnerJpqlVulnerable(entityManager, owner);
            }

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<html><body>");
            out.println("<h1>JPA/Hibernate 查询注入示例</h1>");
            out.println("<p>查询类型：<strong>" + ("native".equals(queryType) ? "原生 SQL" : "JPQL/HQL") + "</strong></p>");
            out.println("<p>当前模式：<strong>" + ("safe".equals(mode) ? "安全" : "脆弱") + "</strong></p>");
            out.println("<p>输入参数 owner：<code>" + HtmlEscaper.escape(owner) + "</code></p>");
            out.println("<p>关键调用：<code>"
                    + buildDescription(queryType, mode)
                    + "</code></p>");
            out.println("<p>查询结果数量：" + orders.size() + "</p>");
            out.println("<ul>");
            for (JpaOrderEntity order : orders) {
                out.println("<li>id=" + order.getId()
                        + ", owner=" + HtmlEscaper.escape(order.getOwner())
                        + ", itemName=" + HtmlEscaper.escape(order.getItemName())
                        + ", status=" + HtmlEscaper.escape(order.getStatus()) + "</li>");
            }
            out.println("</ul>");
            out.println("<p><a href=\"" + req.getContextPath() + "/jpa-demo.jsp\">返回 JPA/Hibernate 演示页</a></p>");
            out.println("</body></html>");
        } finally {
            entityManager.close();
        }
    }

    private String buildDescription(String queryType, String mode) {
        if ("native".equals(queryType)) {
            return "safe".equals(mode)
                    ? "createNativeQuery(...).setParameter(1, owner)"
                    : "createNativeQuery(\"select * ... owner = '\" + owner + \"'\")";
        }
        return "safe".equals(mode)
                ? "createQuery(\"from JpaOrderEntity where owner = :owner\").setParameter(...)"
                : "createQuery(\"from JpaOrderEntity where owner = '\" + owner + \"'\")";
    }
}
