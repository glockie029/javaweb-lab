package com.example.lab01.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (isValidUser(username, password)) {
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", username);
            System.out.println(session);
            resp.sendRedirect(req.getContextPath() + "/home.jsp");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/login.jsp?error=1");
    }

    private boolean isValidUser(String username, String password) {
        return ("admin".equals(username) && "admin123".equals(password))
                || ("alice".equals(username) && "alice123".equals(password))
                || ("bob".equals(username) && "bob123".equals(password));
    }
}
