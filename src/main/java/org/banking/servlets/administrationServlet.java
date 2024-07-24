package org.banking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.banking.database.jdbc;
import org.banking.models.Credential;
import org.banking.models.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name="AdministrationServlet", value="/administration-servlet")
public class administrationServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        jdbc db = new jdbc();
        String action = request.getParameter("action");
        System.out.println(action);
        HttpSession session = request.getSession();

        switch (action) {
            case "register":
                try {
                    session.setAttribute("label", "register-message");
                    User user = db.insertUser(new User(request));
                    response.setContentType("text/plain");
                    response.setHeader("Content-Disposition", "attachment;filename=" + user.get_full_name()+ ".txt");

                    String fileContent = "Account Number: " + user.get_account_number();
                    fileContent += "\nPassword: " + user.get_password();

                    PrintWriter writer = response.getWriter();
                    writer.println(fileContent);
                    writer.close();

                    session.setAttribute("color", "forestgreen");
                    session.setAttribute("message", "Registered Successfully");

                } catch (SQLException | InterruptedException e) {
                    session.setAttribute("color", "red");
                    session.setAttribute("message", e.getMessage());
                } finally {
                    db.close();
                    response.sendRedirect("Admin.jsp");
                }
                break;

            case "modify-fetch":
                session.setAttribute("fetchAccountNo", request.getParameter("account-no"));
                response.sendRedirect("Admin.jsp");
                break;

            case "modify-post":
                session.setAttribute("label", "modify-message");
                try {
                    db.modifyUser(Integer.parseInt((String) session.getAttribute("fetchAccountNo")), request);
                    session.setAttribute("color", "forestgreen");
                    session.setAttribute("message", "Details modified successfully");
                } catch (SQLException e) {
                    session.setAttribute("color", "red");
                    session.setAttribute("message", e.getMessage());
                }
                session.removeAttribute("fetchAccountNo");
                response.sendRedirect("Admin.jsp");
                break;

            case "delete":

                try {
                    int id;
                    String declaration = request.getParameter("declaration");
                    session.setAttribute("label", "delete-message");

                    Credential login = (Credential) session.getAttribute("login");

                    if (!login.is_admin) {
                        id = ((Credential) session.getAttribute("login")).accountNumber;
                        session.setAttribute("color", "red");

                    } else {
                        id = Integer.parseInt(request.getParameter("account-no"));
                    }

                    if (!declaration.equals("delete/" + id))
                        throw new RuntimeException("declaration failed");

                    if (db.fetchBalance(id) != 0)
                        throw new RuntimeException("Balance should be zero");

                    db.deleteUser(id);

                    if (login.is_admin){
                        session.setAttribute("color", "forestgreen");
                        throw new RuntimeException("Deleted account successfully");
                    }

                    session.removeAttribute("label");
                    session.removeAttribute("color");
                    response.sendRedirect("removeAttribute");


                } catch (RuntimeException | SQLException e) {
                    session.setAttribute("message", e.getMessage());
                    response.sendRedirect("customer.jsp");
                } finally {
                    db.close();
                }
                break;

        }
    }
}
