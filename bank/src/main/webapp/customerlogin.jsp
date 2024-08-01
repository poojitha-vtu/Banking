<!DOCTYPE html>
<html>
<head>
    <title>Customer Login</title>
    <link rel="stylesheet" type="text/css" href="css/customerlogin.css">

    <script>
        function saveAccountNumber() {
            // Get the account number from the input field
            var accountNo = document.getElementById("accountNo").value;
            
            // Check if the account number is not empty
            if (accountNo.trim() !== "") {
                // Save the account number to local storage
                localStorage.setItem("accountNo", accountNo);
            } else {
                // Handle the case when the account number is empty
                alert("Please enter your account number.");
                return false; // Prevent form submission
            }
        }

        history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
    </script>
</head>
<body>
    <div class="container">
        <h1 id="loginTitle">Customer Login</h1>
        <form action="CustomerLoginServlet" method="post" onsubmit="return saveAccountNumber()">
            <div class="input-container">
                <label for="accountNo">Account Number:</label>
                <input type="text" id="accountNo" name="accountNo" required>
            </div>
            <div class="input-container">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <input type="submit" id="loginButton" value="Login">
        </form>
    </div>
</body>
</html>
