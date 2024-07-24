package org.banking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.banking.database.jdbc;
import org.banking.models.Credential;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="ResetServler", value="/reset-servlet")
public class resetServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        jdbc db = new jdbc();
        HttpSession session = request.getSession();
        session.setAttribute("label", "reset-message");
        int account = ((Credential) session.getAttribute("login")).accountNumber;
        String password = request.getParameter("new-password");
        System.out.println(password +  " " + request.getParameter("confirm-new-password"));
        try {
            db.checkPassword(account, request.getParameter("password"));
            System.out.println("Current password: " + password);
            if (password.equals(request.getParameter("confirm-new-password"))) {
                System.out.println("New password confirmed");
                db.updatePassword(account, password);

            } else
                throw new RuntimeException("Password doesn't match");


            session.setAttribute("color", "forestgreen");
            session.setAttribute("message", "Password Reset Successful");
        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
            session.setAttribute("color", "red");
            session.setAttribute("message", e.getMessage());
        }

        response.sendRedirect("customer.jsp");

    }

}
