<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.banking.models.Credential" %>

<%
    Credential login = (Credential) session.getAttribute("login");

    if (login != null) {
        if (login.is_admin)
            response.sendRedirect("Admin.jsp");
        else
            response.sendRedirect("customer.jsp");
    } else {
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        form {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
        }

        h2 {
            text-align: center;
            color: #333333;
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            color: #333333;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #cccccc;
            border-radius: 5px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 5px;
        }

        button:hover {
            background-color: #0053ac;
        }
    </style>
</head>
<body>
    <form action="login-servlet" method="post">

        <h2>Login</h2>

        <label for="Account_No">Account No:</label>
        <input type="text" id="Account_No" name="account_no" pattern="^[1-9][0-9]{4}$" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <button>Login</button><br>

        <div id="error-message"></div>

    </form>

    <%
        String status = (String) session.getAttribute("status");
        session.removeAttribute("status");
    %>

    <script type="text/javascript">
        const error = "<%= status != null ? status : "" %>";
        if (error) {
            document.getElementById("error-message").innerHTML = error;
            document.getElementById("error-message").style.setProperty("color", "red");
        }
    </script>
</body>
</html>

<%
    }
%>
