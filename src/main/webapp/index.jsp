<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JESBO BANK - Welcome</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background-image: url('https://via.placeholder.com/1500'); /* Replace with your background image URL */
            background-size: cover;
            background-position: center;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .header {
            position: absolute;
            top: 20px;
            left: 20px;
            font-size: 1.5em;
            font-weight: bold;
            color: #fff;
        }
        .container {
            text-align: center;
            background-color: rgba(255, 255, 255, 0.95); /* semi-transparent white background */
            padding: 60px 80px;
            border-radius: 10px;
            box-shadow: 0 0 30px rgba(0, 0, 0, 0.2);
            max-width: 800px;
            width: 100%;
            position: relative;
        }
        .welcome-text {
            font-size: 3em;
            color: #4CAF50;
            margin-bottom: 20px;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
        }
        h1 {
            font-size: 2.5em;
            margin-bottom: 15px;
            color: #333; /* darker text color */
        }
        p {
            font-size: 1.3em;
            color: #666; /* medium grey text color */
            margin-bottom: 30px;
        }
        .login-button {
            display: inline-block;
            padding: 15px 30px;
            font-size: 18px;
            color: #fff;
            background-color: #007bff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            position: relative;
            transition: background-color 0.3s ease;
        }
        .login-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="header">UI BANK</div>
<div class="container">
    <div class="welcome-text">Welcome</div>
    <h1>Welcome to UI BANK</h1>
    <p>Your trusted partner in financial success. Our mission is to provide you with the best banking services to help you achieve your financial goals. Join us today and experience the difference.</p>
    <a href="login-servlet" class="login-button">Login</a>
</div>
</body>
</html>