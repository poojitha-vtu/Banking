<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bank.DatabaseConnection" %>
<%@ page import="bank.CustomerDAO" %>
<%@ page import="bank.Customer" %>
<%@ page import="bank.TransactionDAO" %>
<%@ page import="bank.Transaction" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>

<%
    if (Objects.isNull(session.getAttribute("accountNo"))) {
        response.sendRedirect("customerlogin.jsp");
        return;
    }

    if (session.getAttribute("refreshed") == null) {
        session.setAttribute("refreshed", true);
        response.setHeader("Refresh", "1");
    }
%>

<%
response.setHeader("Cache-Control","no-store");
response.setHeader("Pragma","no-cache"); 
response.setHeader ("Expires", "0");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Dashboard</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.11.338/pdf.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.9.3/html2pdf.bundle.min.js"></script>
    <script type="text/javascript">
        history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
    </script>
    <link rel="stylesheet" type="text/css" href="./css/customer_dashboard.css">
</head>
<body>
<div class="container">
    <h1>Welcome <%= session.getAttribute("accountNo") %> to Your Dashboard</h1>

    <%
        double accountBalance = 0;
        String accountNoStr = session.getAttribute("accountNo").toString();
        int accountNo = Integer.parseInt(accountNoStr);
        try {
            Customer customer = CustomerDAO.getCustomerByAccountNo(accountNo);
            if (customer != null) {
                accountBalance = customer.getInitialBalance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error retrieving account balance: " + e.getMessage() + "</p>");
        }
    %>

    <h2>Account Balance: $<%= accountBalance %></h2>

    <div class="button-container">
        <div class="button-group-left">
            <button onclick="openAddMoneyAlert()">Add Money</button>
            <button onclick="openRemoveMoneyAlert()">Remove Money</button>
            <button onclick="downloadTable()">Download PDF</button>
        </div>
        <div class="button-group-right">
            <button onclick="resetPassAlert()">Reset Password</button>
            <button onclick="deleteAcc()">Delete Account</button>
            <button class="logout-button" onclick="logout()">Logout</button>
        </div>
    </div>

    <h2>Recent Transactions:</h2>
    <div class="sort-container">
        <label for="sortOrder">Sort Order:</label>
        <select id="sortOrder" onchange="sortTable()">
            <option value="asc">Ascending</option>
            <option value="desc">Descending</option>
        </select>
    </div>

    <div class="transactions-table">
        <table border="1" id="transactions">
            <thead>
                <tr>
                    <th>Transaction ID</th>
                    <th>Account No</th>
                    <th>Transaction Type</th>
                    <th>Amount</th>
                    <th>Transaction Date</th>
                </tr>
            </thead>
            <tbody>
                <% 
                try {
                    List<Transaction> transactions = TransactionDAO.getRecentTransactions(accountNoStr);
                    int counter = 0;
                    for (Transaction transaction : transactions) {
                %>
                    <tr>
                        <td><%= ++counter %></td>
                        <td><%= transaction.getAccountNo() %></td>
                        <td><%= transaction.getTransactionType() %></td>
                        <td><%= transaction.getAmount() %></td>
                        <td><%= transaction.getTransactionDate() %></td>
                    </tr>
                <% 
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    out.println("<tr><td colspan='5'>Error fetching transactions: " + e.getMessage() + "</td></tr>");
                }
                %>
            </tbody>
        </table>
    </div>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.9.3/html2pdf.bundle.min.js"></script>

    <script>
        function sortTable() {
            var table = document.getElementById("transactions");
            var sortOrder = document.getElementById("sortOrder").value;
            var rows, switching, i, x, y, shouldSwitch;
            switching = true;
            while (switching) {
                switching = false;
                rows = table.rows;
                for (i = 1; i < (rows.length - 1); i++) {
                    shouldSwitch = false;
                    x = rows[i].getElementsByTagName("TD")[4].innerText;
                    y = rows[i + 1].getElementsByTagName("TD")[4].innerText;
                    if (sortOrder === "asc") {
                        if (x > y) {
                            shouldSwitch = true;
                            break;
                        }
                    } else if (sortOrder === "desc") {
                        if (x < y) {
                            shouldSwitch = true;
                            break;
                        }
                    }
                }
                if (shouldSwitch) {
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                }
            }
        }

        function resetPassAlert() {
            var newPass = prompt("Enter new Password:");
            if (newPass !== null) {
                resetPassword(newPass);
            }
        }

        function openAddMoneyAlert() {
            var amountToAdd = prompt("Enter amount to add:");
            if (amountToAdd !== null) {
                addMoney(amountToAdd);
            }
        }

        function openRemoveMoneyAlert() {
            var amountToRemove = prompt("Enter amount to remove:");
            if (amountToRemove !== null) {
                removeMoney(amountToRemove);
            }
        }

        function resetPassword(newPassword) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "ResetPasswordServlet");
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        alert("Password reset successful!");
                    } else {
                        alert("Error: " + xhr.responseText);
                    }
                }
            };
            xhr.send("newPassword=" + encodeURIComponent(newPassword));
        }

        function addMoney(amountToAdd) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "AddMoneyServlet");
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        location.reload();
                    } else {
                        alert("Error: " + xhr.responseText);
                    }
                }
            };
            xhr.send("amountToAdd=" + encodeURIComponent(amountToAdd));
        }

        function removeMoney(amountToRemove) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "RemoveMoneyServlet");
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        location.reload();
                    } else {
                        alert("Error: " + xhr.responseText);
                    }
                }
            };
            xhr.send("amountToRemove=" + encodeURIComponent(amountToRemove));
        }

        function logout() {
            window.location.href = "CustomerLogoutServlet"; // Adjust the URL if needed
        }

        function downloadTable() {
            // Get the table element
            var table = document.getElementById("transactions");

            // Create configuration object for html2pdf
            var opt = {
                margin: 10,
                filename: 'bank_statement.pdf',
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2 },
                jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
            };

            // Use html2pdf to generate and download PDF
            html2pdf().from(table).set(opt).save();
        }
    </script>
</div>

<script src="noback.js"></script>

</body>
</html>
