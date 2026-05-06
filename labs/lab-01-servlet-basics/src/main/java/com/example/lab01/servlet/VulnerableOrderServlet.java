package com.example.lab01.servlet;

import com.example.lab01.model.OrderRecord;
import com.example.lab01.repository.OrderRepository;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VulnerableOrderServlet extends HttpServlet {

    private final OrderRepository orderRepository = new OrderRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer orderId = parseOrderId(req.getParameter("id"));
        if (orderId == null) {
            renderBadRequest(req, resp);
            return;
        }

        String currentUser = String.valueOf(req.getSession(false).getAttribute("currentUser"));
        OrderRecord order = orderRepository.findById(orderId.intValue());

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>脆弱示例：仅按订单 id 查询</h1>");
        out.println("<p>当前登录用户：<strong>" + currentUser + "</strong></p>");
        out.println("<p>持久层调用：<code>findById(" + orderId + ")</code></p>");

        if (order == null) {
            out.println("<p>未找到订单。</p>");
        } else {
            out.println("<p>订单 id：" + order.getId() + "</p>");
            out.println("<p>订单所有者：" + order.getOwner() + "</p>");
            out.println("<p>商品名称：" + order.getItemName() + "</p>");
            out.println("<p>数量：" + order.getAmount() + "</p>");
            out.println("<p style=\"color:red;\">问题：即使当前用户不是订单所有者，只要知道 id，也能读到这条记录。</p>");
        }

        out.println("<p><a href=\"" + req.getContextPath() + "/home.jsp\">返回首页</a></p>");
        out.println("</body></html>");
    }

    private Integer parseOrderId(String rawId) {
        if (rawId == null || rawId.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(rawId);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void renderBadRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>400 Bad Request</h1>");
        out.println("<p>请使用合法的订单 id，例如：?id=1001</p>");
        out.println("<p><a href=\"" + req.getContextPath() + "/home.jsp\">返回首页</a></p>");
        out.println("</body></html>");
    }
}
