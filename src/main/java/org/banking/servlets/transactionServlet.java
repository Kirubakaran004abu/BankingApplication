package org.banking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.banking.models.Credential;
import org.banking.models.Transaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/transaction")
public class transactionServlet extends HttpServlet{

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String source = req.getParameter("source");
        int account = ((Credential) req.getSession().getAttribute("login")).accountNumber;
        HttpSession session = req.getSession();

        switch (source) {
            case "deposit":
                session.setAttribute("label", "deposit-message");
                Transaction.message = "self deposit";
                try {
                    Transaction.deposit(Double.parseDouble(req.getParameter(source)),
                            account);
                    session.setAttribute("color", "forestgreen");
                    session.setAttribute("message", "Deposit successful");
                } catch (SQLException e) {
                    session.setAttribute("color", "red");
                    session.setAttribute("message", e.getMessage());
                }
                break;

            case "withdraw":
                session.setAttribute("label", "withdraw-message");
                Transaction.message = "self withdraw";
                try {
                    Transaction.withdraw(Double.parseDouble(req.getParameter(source)),
                            account);
                    session.setAttribute("color", "forestgreen");
                    session.setAttribute("message", "Withdraw successful");
                } catch (SQLException e) {
                    session.setAttribute("color", "red");
                    session.setAttribute("message", e.getMessage());
                }
                break;

            case "transfer":
                session.setAttribute("label", "transfer-message");
                try {
                    Transaction.message = "fund transfer to ";
                    Transaction.transfer(Double.parseDouble(req.getParameter(source)),
                            account, Integer.parseInt(req.getParameter("transfer-account-number")));
                    session.setAttribute("color", "forestgreen");
                    session.setAttribute("message", "Transfer successful");
                } catch (SQLException e) {
                    req.getSession().setAttribute("transfer-error", e.getMessage());
                    session.setAttribute("color", "red");
                    session.setAttribute("message", e.getMessage());
                }
                break;
        }

        res.sendRedirect("customer.jsp");

    }

}
