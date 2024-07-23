package org.banking.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.banking.models.Credential;
import org.banking.models.Transaction;

@WebServlet("/transaction")
public class transactionServlet extends HttpServlet{

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String source = req.getParameter("source");
        int account = ((Credential) req.getSession().getAttribute("login")).accountNumber;

        PrintWriter out = res.getWriter();
        switch (source) {
            case "deposit":
                Transaction.message = "self deposit";
                Transaction.deposit(Double.parseDouble(req.getParameter(source)),
                        account);
                break;

            case "withdraw":
                Transaction.message = "self withdraw";
                Transaction.withdraw(Double.parseDouble(req.getParameter(source)),
                        account);
                break;

            case "transfer":
                Transaction.message = "fund transfer to ";
                Transaction.transfer(Double.parseDouble(req.getParameter(source)),
                        account,
                        Integer.parseInt(req.getParameter("transfer-account-number")));
                break;
        }

        res.sendRedirect("customer.jsp");

    }

}
