<!DOCTYPE html>
<html>
<head>
    <title>Customer Login</title>
    <link rel="stylesheet" type="text/css" href="./css/customerlogin.css">
    <script>
        function saveAccountNumber() {
            var accountNo = document.getElementById("accountNo").value;
            if (accountNo.trim() !== "") {
                localStorage.setItem("accountNo", accountNo);
            } else {
                alert("Please enter your account number.");
            }
        }

        function selectLanguage(language) {
            localStorage.setItem("language", language);
            document.getElementById("welcome-box").style.display = "none";
            document.getElementById("login-box").style.display = "block";
            if (language === "telugu") {
                document.getElementById("login-title").innerText = "కస్టమర్ లాగిన్";
                document.getElementById("accountNoLabel").innerText = "ఖాతా సంఖ్య:";
                document.getElementById("passwordLabel").innerText = "పాస్వర్డ్:";
                document.getElementById("submitBtn").value = "లాగిన్";
            }
        }

        window.onload = function() {
            var language = localStorage.getItem("language");
            if (language) {
                selectLanguage(language);
            }
        }
    </script>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        #welcome-box, #login-box {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        #welcome-box {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
        }
        #login-box {
            display: none;
            flex-direction: column;
            gap: 10px;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .btn-english {
            background-color: #4CAF50;
            color: white;
        }
        .btn-telugu {
            background-color: #008CBA;
            color: white;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 5px 0 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <div id="welcome-box">
            <h1>Welcome to Genpact Bank Login</h1>
            <p>Please select your preferable language:</p>
            <button class="btn btn-english" onclick="selectLanguage('english')">English</button>
            <button class="btn btn-telugu" onclick="selectLanguage('telugu')">తెలుగు</button>
        </div>
        <div id="login-box">
            <h1 id="login-title">Customer Login</h1>
            <form action="CustomerLoginServlet" method="post" onsubmit="return saveAccountNumber()">
                <label for="accountNo" id="accountNoLabel">Account Number:</label>
                <input type="text" id="accountNo" name="accountNo" required=""><br>
                
                <label for="password" id="passwordLabel">Password:</label>
                <input type="password" id="password" name="password" required=""><br>
                
                <input type="submit" id="submitBtn" value="Login">
            </form>
        </div>
    </div>

    <script>
        history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
    </script>
</body>
</html>
