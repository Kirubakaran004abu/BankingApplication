package org.banking.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.banking.database.jdbc;
import org.banking.models.Credential;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class loginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("login.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        jdbc db = new jdbc();
        int acc = Integer.parseInt(request.getParameter("account_no"));
        String pass = request.getParameter("password");
        HttpSession session = request.getSession();
        try {
            if (!db.checkAccountExist(acc))
                throw new SQLException("Account does not exist");

            Credential login = db.checkPassword(acc, pass);

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