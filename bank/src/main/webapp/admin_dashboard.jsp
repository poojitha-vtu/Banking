<%@ page import="java.util.List" %>
<%@ page import="bank.Customer" %>
<%@ page import="bank.CustomerDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
response.setHeader("Cache-Control", "no-store");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0"); // Prevents caching at the proxy server

List<Customer> customers = null;
try {
    customers = CustomerDAO.getAllCustomers();
} catch (Exception e) {
    e.printStackTrace();
    response.sendRedirect("error.jsp");
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css" href="css/admin_dashboard.css">
</head>
<body>
<div style="display: flex; flex-direction: row; justify-content: space-between; margin: 0px 85px">
    <h2 style="margin-left: 10rem; margin-top: 2rem;">Welcome Admin</h2>
    <div class="button-container">
        <button class="logout-button" onclick="logout()">Logout</button>
        <a href="register.jsp"><button class="new-user-button">REGISTER A NEW USER</button></a>
    </div>
</div>
<h1>Admin Dashboard</h1>
<table border="1">
    <thead>
        <tr>
            <th>Account No</th>
            <th>Full Name</th>
            <th>Address</th>
            <th>Mobile No</th>
            <th>Email</th>
            <th>Account Type</th>
            <th>Initial Balance</th>
            <th>Date of Birth</th>
            <th>ID Proof</th>
            <th>Password</th>
            <th>First Login</th>
            <th>Actions</th> <!-- Add this line -->
        </tr>
    </thead>
    <tbody>
        <% if (customers != null) { %>
            <% for (Customer customer : customers) { %>
            <tr>
                <td><%= customer.getAccountNo() %></td>
                <td><%= customer.getFullName() %></td>
                <td><%= customer.getAddress() %></td>
                <td><%= customer.getMobileNo() %></td>
                <td><%= customer.getEmail() %></td>
                <td><%= customer.getAccountType() %></td>
                <td><%= customer.getInitialBalance() %></td>
                <td><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(customer.getDob()) %></td>
                <td><%= customer.getIdProof() %></td>
                <td><%= customer.getPassword() %></td>
                <td><%= customer.isFirstLogin() %></td>
                <td style="display: flex;">
                    <a style="margin: 4px" href="update_customer.jsp?account_no=<%= customer.getAccountNo() %>">
                        <button style="background-color: #007bff" class="delete-button">Update</button>
                    </a>
                    <a style="margin: 4px" href="delete_customer.jsp?account_no=<%= customer.getAccountNo() %>" onclick="return confirm('Are you sure?')">
                        <button class="delete-button">Delete</button>
                    </a>
                </td>
            </tr>
            <% } %>
        <% } %>
    </tbody>
</table>
<script>
    function logout() {
        window.location.href = "index.jsp";
    }
</script>

<script type="text/javascript">
    window.history.forward();
    function noBack() {
        window.history.forward();
    }
</script>
</body>
</html>
