<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.banking.database.jdbc" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.banking.models.Credential" %>

<%
    Credential login = (Credential) session.getAttribute("login");
    if (login == null || login.is_admin) {
        response.sendRedirect("login.jsp");
    }
    else {
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Banking Tabs</title>
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
            max-height: 700px;
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
            background-color: #0056b3;
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
        <%
            jdbc db = new jdbc();
            double balance;

            try {
                balance = db.fetchBalance(login.accountNumber);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        %>
        <div id="welcome-text"><h4>Hello <%=login.name%>, Your balance ₹<%=balance%></h4></div>
        <a href="removeAttribute"><button class="logout-button">Logout</button></a>

        <div class="tabs">
            <button class="tab-button" data-tab="deposit">Deposit</button>
            <button class="tab-button" data-tab="withdrawal">Withdrawal</button>
            <button class="tab-button" data-tab="fund-transfer">Fund Transfer</button>
            <button class="tab-button" data-tab="mini-statement">Mini Statement</button>
            <button class="tab-button" data-tab="reset-password">Reset Password</button>
            <button class="tab-button" data-tab="delete-account">Delete Account</button>
        </div>

        <div id="deposit" class="tab-content">
            <form action="transaction" method="post">
                <input type="hidden" name="source" value="deposit">

                <input type="text" id="deposit-amount" name="deposit" placeholder="Enter amount to deposit"
                       pattern="^(0\.[1-9]\d?|[1-9]\d*(\.\d{1,2})?)$" title="Enter valid amount" required><br>

                <button>Deposit</button>
            </form>
        </div>

        <div id="withdrawal" class="tab-content">
            <form action="transaction" method="post">
                <input type="hidden" name="source" value="withdraw">

                <input type="text" id="withdrawal-amount" name="withdraw" placeholder="Enter Amount to Withdraw "
                       pattern="^(0\.[1-9]\d?|[1-9]\d*(\.\d{1,2})?)$" title="Enter valid amount" required><br>

                <button>Withdraw</button>
            </form>
        </div>

        <div id="fund-transfer" class="tab-content">
            <form action="transaction" method="post">
                <input type="hidden" name="source" value="transfer">

                <input type="text" id="transfer-account-number" name="transfer-account-number" placeholder="Enter the Account Number"
                       pattern="^[1-9]\d{4}$" title="Enter valid Account number" required><br>

                <input type="text" id="transfer-amount" name="transfer" placeholder="Enter the Amount "
                       pattern="^0\.[1-9]\d?|[1-9]\d*(\.\d{1,2})?$" title="Enter valid amount" required><br>

                <button>Transfer</button>
            </form>
        </div>

        <div id="mini-statement" class="tab-content">
            <table id="statement-table">

                    <tr>
                        <th>Date</th>
                        <th>Description</th>
                        <th>Amount</th>
                        <th>Balance</th>
                    </tr>

                    <%;
                        try {
                            ResultSet rs = db.fetchTransaction(login.accountNumber);
                            while (rs.next()) {
                    %>
                    <tr>
                        <td><%= rs.getDate("date_time") %></td>
                        <td><%= rs.getString("description") %></td>
                        <td>₹ <%= rs.getDouble("amount") %></td>
                        <td>₹ <%= rs.getDouble("new_balance") %></td>
                    </tr>
                    <%
                            }
                        } catch (SQLException e) {
                            %><%="Error Occured"%><%
                            System.out.println(e.getMessage());
                        } finally {
                            db.close();
                        }
                    %>
            </table>    
        </div>

        <div id="reset-password" class="tab-content">
            <form action="reset-servlet" method="post">

                <input type="text" id="rp-password" name="password" placeholder="Enter Current Password" required><br>
                <input type="text" id="rp-new-password" name="new-password" placeholder="Enter New Password" required><br>
                <input type="text" id="rp-confirm-new-password" name="confirm-new-password" placeholder="Confirm New Password" required><br>

                <button>Reset</button>

            </form>
        </div>

        <div id="delete-account" class="tab-content">
            <form action="" method="post">

                <input type="hidden" name="action" value="delete">

                <input type="text" id="delete-account-no" name="account-no" placeholder="Account No" required><br><br>

                <label for="delete-declaration">Enter "delete/account no" to delete</label><br>
                <input type="text" id="delete-declaration" name="declaration" placeholder='' required><br>

                <button type="submit" class="delete-button">Delete User</button>

            </form>
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

        <%
            String status = null;
            if (session != null) {
                status = (String) session.getAttribute("status");
                session.removeAttribute("status"); // Remove error attribute after displaying
            }
        %>

        const error = "<%= status != null ? status : "" %>";
        if (error) {
            document.getElementById("error-message").innerHTML = error;
        }

    </script>
</body>
</html>

<%
    }
%>