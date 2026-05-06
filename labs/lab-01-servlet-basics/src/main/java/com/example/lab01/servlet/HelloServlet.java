package com.example.lab01.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello from traditional JavaWeb</h1>");
        out.println("<p>Servlet mapping is working.</p>");
        out.println("<p><a href=\"" + req.getContextPath() + "/\">Back to index</a></p>");
        out.println("</body></html>");
    }
}
