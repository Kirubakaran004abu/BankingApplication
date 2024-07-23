package org.banking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.banking.database.jdbc;
import org.banking.models.Credential;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="ResetServler", value="/reset-servlet")
public class resetServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        jdbc db = new jdbc();
        HttpSession session = request.getSession();
        int account = ((Credential) session.getAttribute("login")).accountNumber;
        String password = request.getParameter("new-password");
        System.out.println(password +  " " + request.getParameter("confirm-new-password"));
        try {
            if (db.checkPassword(account, request.getParameter("password")).logged_in) {
                System.out.println("Current password: " + password);
                if (password.equals(request.getParameter("confirm-new-password"))) {
                    System.out.println("New password confirmed");
                    db.updatePassword(account, password);
                    response.sendRedirect("removeAttribute");
                } else
                    throw new RuntimeException("Password doesn't match");
            } else {
                throw new RuntimeException("Incorrect password");
            }
        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
            session.setAttribute("status", e.getMessage());
            response.sendRedirect("customer.jsp");
        }

    }

}
