<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.banking.database.jdbc" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.banking.models.Credential" %>
<%@ page import="org.banking.models.User" %>

<%
    Credential login = (Credential) session.getAttribute("login");

    if (login == null || !login.is_admin) {
        response.sendRedirect("login.jsp");
    }
    else {
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .tabs-container {
            position: absolute;
            top: 50px;
            background-color: #fff;
            width: 80%;
            /*max-height: 700px;*/
            max-width: 1200px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            overflow: hidden;
        }
        .tabs {
            display: flex;
            justify-content: space-around;
            background-color: #007bff;
        }
        .tabs button {
            flex: 1;
            padding: 20px 0;
            font-size: 1.2em;
            color: #fff;
            background-color: #007bff;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .tabs button:hover {
            background-color: #005ccf;
        }
        .tabs button.active {
            background-color: #0056b3;
        }
        .tab-content {
            padding: 20px;
            display: none;
        }
        .tab-content.active {
            display: block;
        }
        form {
            position: relative;
            width: 70%;
            height: 50%;
            justify-self: center;
            align-self: center;
        }
        input,
        button,
        select {
            width: 50%;
            padding: 15px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 1em;
        }
        button {
            width: 30%;
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #0056b3;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        td:nth-child(3),td:nth-child(4){
            text-align: right;
        }
        th {
            background-color: #f2f2f2;
            color: #333;
        }
        tr:hover {
            background-color: #f9f9f9;
        }
        .logout-button {
            position: absolute;
            top: 0;
            right: 10px;
            padding: 10px 20px;
            background-color: #dc3545;
            color: white;
            border: none;
            border-radius: 5px;
            width: 100px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .logout-button:hover {
            background-color: #c82333;
        }
        #welcome-text {
            position: relative;
            display: flex;
            justify-content: center;
            align-content: center;
        }
    </style>
</head>
<body>

    <div class="tabs-container">
        <div id="welcome-text"><h4>Hello <%=login.name%></h4></div>
        <a href="removeAttribute"><button class="logout-button">Logout</button></a>

        <div class="tabs">
            <button class="tab-button" data-tab="details">User Details</button>
            <button class="tab-button" data-tab="register">Register User</button>
            <button class="tab-button" data-tab="modify">Update User</button>
            <button class="tab-button" data-tab="reset-password">Reset Password</button>
            <button class="tab-button" data-tab="delete">Delete Account</button>
        </div>

        <div id="details" class="tab-content">
            <table>
                <tr>
                    <th>Account No</th>
                    <th>Name</th>
                    <th>Mobile</th>
                    <th>Email</th>
                    <th>DOB</th>
                </tr>

                <%
                    jdbc db = new jdbc();
                    try {
                        ResultSet rs = db.fetchUsers();
                        while (rs.next()) {
                %>
                <tr>
                    <td><%= rs.getInt("account_no") %></td>
                    <td><%= rs.getString("username") %></td>
                    <td><%= rs.getString("mobile") %></td>
                    <td><%= rs.getString("email") %></td>
                    <td><%= rs.getDate("date_of_birth") %></td>
                </tr>
                <%
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                %>
            </table>
        </div>

        <div id="register" class="tab-content">
            <div class="container">
                <form action="administration-servlet" method="post">
                    <input type="hidden" name="action" value="register">

                    <label for="Full_name">Full Name:</label>
                    <input type="text" id="Full_Name" name="full_name" pattern="^[a-zA-Z\s]+$" required><br>

                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" required><br>

                    <label for="Mobile_No">Mobile No:</label>
                    <input type="text" id="Mobile_No" name="mobile" pattern="^[1-9][0-9]{9}$" required><br>

                    <label for="register-email">Email:</label>
                    <input type="email" id="register-email" name="email" required><br>

                    <label for="account_type">Account Type:</label>
                    <select id="account_type" name="account_type" required>
                        <option value="Savings">Savings</option>
                        <option value="Current">Current</option>
                        <option value="Admin">Admin</option>
                    </select><br>

                    <label for="date_of_birth">Date Of Birth:</label>
                    <input type="date" id="date_of_birth" name="date_of_birth" required><br>

                    <label for="ID_Proof">ID Proof:</label>
                    <select id="ID_Proof" name="ID_Proof">
                        <option value="" disabled selected>--select--</option>
                        <option value="Aadhaar Card">Aadhaar Card</option>
                        <option value="Driving License">Driving License</option>
                        <option value="Passport">Passport</option>
                        <option value="Pan Card">Pan Card</option>
                    </select><br>

                    <button>Register</button>
                </form>

            </div>
        </div>


        <div id="modify" class="tab-content">
            <div class="container">
                <form action="administration-servlet" method="post">
                    <input type="hidden" name="action" value="modify-fetch"><br>

                    <label for="modify-account-no">Account No</label>
                    <input type="text" id="modify-account-no" name="account-no"
                           pattern="^[1-9][0-9]{4}$" required><br>

                    <button type="submit" class="submit-button">Fetch User</button>
                </form>

                <%
                    String fetchAccount = (String) session.getAttribute("fetchAccountNo");
                    if (!(fetchAccount == null)){
                        try {
                            ResultSet user = db.fetchUser(Integer.parseInt(fetchAccount));

                %>

                <form action="administration-servlet" method="post">

                    <input type="hidden" name="action" value="modify-post">

                    <label for="modify-username">Username</label>
                    <input type="text" id="modify-username" name="username" pattern="^[a-zA-Z\s]+$"
                           placeholder="<%=user.getString("username")%>"><br>

                    <label for="modify-email">Email</label>
                    <input type="email" id="modify-email" name="email" placeholder="<%=user.getString("email")%>"><br>

                    <label for="modify-mobile">Mobile</label>
                    <input type="text" id="modify-mobile" name="mobile" pattern="^[1-9][0-9]{9}$"
                           placeholder="<%=user.getString("mobile")%>"><br>

                    <label for="address">Address</label>
                    <input type="text" id="modify-address" name="address" placeholder="<%=user.getString("Address")%>"><br>

                    <label for="modify-date-of-birth">Date of Birth</label>
                    <input type="date" id="modify-date-of-birth" name="date_of_birth" value="<%=user.getDate("date_of_birth")%>"><br>

                    <%
                        if (user.getString("id_proof") == null) {
                    %>

                    <label for="modify_ID_Proof">ID Proof:</label>
                    <select id="modify_ID_Proof" name="id_proof">
                        <option value="" disabled selected>--select--</option>
                        <option value="Aadhaar Card">Aadhaar Card</option>
                        <option value="Driving License">Driving License</option>
                        <option value="Passport">Passport</option>
                        <option value="Pan Card">Pan Card</option>
                    </select><br>

                    <%
                        }
                    %>

                    <button type="submit" class="submit-button">Modify User</button>
                </form>

                <%
                        } catch (SQLException e) {
                %>
                <div id="error-label" style="color: red;"><%=e.getMessage()%></div>
                <%
                            System.out.println(e.getMessage());
                        } finally {
                            db.close();
                        }
                    }
                %>

                <div id="modify-label" style="color: forestgreen;"></div>
            </div>
        </div>

        <div id="reset-password" class="tab-content">
            <form action="reset-servlet" method="post">

                <input type="password" id="rp-password" name="password" placeholder="Enter Current Password" required><br>
                <input type="password" id="rp-new-password" name="new-password" placeholder="Enter New Password" required><br>
                <input type="password" id="rp-confirm-new-password" name="confirm-new-password" placeholder="Confirm New Password" required><br>

                <button>Reset</button>

            </form>
        </div>

        <div id="delete" class="tab-content">
            <div class="container">
                <form action="administration-servlet" method="post">
                    <input type="hidden" name="action" value="delete">

                    <input type="hidden" name="action" value="delete">

                    <input type="text" id="delete-account-no" name="account-no" placeholder="Account No" required><br><br>

                    <label for="delete-declaration">Enter "delete/account no" to delete</label><br>
                    <input type="text" id="delete-declaration" name="declaration" placeholder='' required><br>

                    <button type="submit" class="delete-button">Delete User</button>

                </form>
            </div>
        </div>
    </div>

<script>
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    // Function to set the active tab based on localStorage
    function setActiveTab() {
        const activeTabId = sessionStorage.getItem('activeTab');

        if (activeTabId && document.getElementById(activeTabId)) {
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            const activeButton = document.querySelector(`.tab-button[data-tab='` + activeTabId + `']`);
            const activeContent = document.getElementById(activeTabId);

            if (activeButton && activeContent) {
                activeButton.classList.add('active');
                activeContent.classList.add('active');
            }
        } else {
            tabButtons[0].classList.add('active');
            tabContents[0].classList.add('active');
        }
    }

    // Event listener to handle tab clicks
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            const tab = button.getAttribute('data-tab');

            // Remove 'active' class from all buttons and contents
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            // Add 'active' class to the clicked button and corresponding content
            button.classList.add('active');
            document.getElementById(tab).classList.add('active');

            // Store the active tab ID in localStorage
            sessionStorage.setItem('activeTab', tab);
        });
    });

    document.addEventListener('DOMContentLoaded', setActiveTab);

    let timeout;
    const maxIdleTime = 5 * 60 * 1000;

    function logout() {
        window.location.href = 'removeAttribute';
    }

    function resetTimer() {
        clearTimeout(timeout);
        timeout = setTimeout(logout, maxIdleTime);
    }

    window.onload = function() {
        document.onmousemove = resetTimer;
        document.onscroll = resetTimer;
        resetTimer();
    };
</script>

</body>
</html>
<%
    }
%>