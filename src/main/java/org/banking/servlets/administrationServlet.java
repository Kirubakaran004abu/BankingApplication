package org.banking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.banking.database.jdbc;
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
                    User user = db.insertUser(new User(request));
                    response.setContentType("text/plain");
                    response.setHeader("Content-Disposition", "attachment;filename=" + user.get_full_name()+ ".txt");

                    String fileContent = "Account Number: " + user.get_account_number();
                    fileContent += "\nPassword: " + user.get_password();

                    PrintWriter writer = response.getWriter();
                    writer.println(fileContent);
                    writer.close();
                    response.sendRedirect("Admin.jsp");
                } catch (SQLException | InterruptedException e) {
                    response.getWriter().println(e.getMessage());
                    throw new RuntimeException(e);
                } finally {
                    db.close();
                }
                break;

            case "modify-fetch":
                session.setAttribute("fetchAccountNo", request.getParameter("account-no"));
                response.sendRedirect("Admin.jsp");
                break;

            case "modify-post":
                try {
                    db.modifyUser(Integer.parseInt((String) session.getAttribute("fetchAccountNo")), request);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                session.removeAttribute("fetchAccountNo");
                response.sendRedirect("Admin.jsp");
                break;

            case "delete":

                try {
                    int id = Integer.parseInt(request.getParameter("account-no"));
                    String declaration = request.getParameter("declaration");

                    if (declaration.equals("delete/" + id) && db.fetchBalance(id) == 0) {
                        db.deleteUser(id);
                        response.getWriter().println("Successfully deleted");
                    } else {
                        response.getWriter().println("Deletion failed");
                    }

                } catch (SQLException e) {
                    response.getWriter().println(e.getMessage());
                    throw new RuntimeException(e);
                } finally {
                    db.close();
                }
                break;

        }


    }
}
