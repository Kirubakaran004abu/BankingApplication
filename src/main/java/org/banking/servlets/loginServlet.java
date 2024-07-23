package org.banking.servlets;

import java.io.*;
import java.sql.SQLException;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import org.banking.database.jdbc;
import org.banking.models.Credential;
import org.banking.models.User;

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class loginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("login.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        jdbc db = new jdbc();
        String acc = request.getParameter("account_no");
        String pass = request.getParameter("password");
        HttpSession session = request.getSession();
        try {
            Credential login = db.checkPassword(Integer.parseInt(acc), pass);

            if (!login.logged_in) {
                response.sendRedirect("Failure.jsp");
                return;
            }

            session.setAttribute("login", login);

            if (login.is_admin)
                response.sendRedirect("Admin.jsp");
            else
                response.sendRedirect("customer.jsp");


        } catch (SQLException e) {
            session.setAttribute("status", e.getMessage());
            response.sendRedirect("login.jsp");
        } finally {
            db.close();
        }
    }
}