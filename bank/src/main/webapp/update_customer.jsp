<%@ page import="bank.Customer" %>
<%@ page import="bank.CustomerDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
response.setHeader("Cache-Control", "no-store");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0"); // Prevents caching at the proxy server

String accountNo = request.getParameter("account_no");
Customer customer = null;

try {
    customer = CustomerDAO.getCustomerByAccountNo(Integer.parseInt(accountNo));
} catch (Exception e) {
    e.printStackTrace();
    response.sendRedirect("error.jsp");
}
%>

<html>
<head>
    <title>Update Customer</title>
    <link rel="stylesheet" type="text/css" href="./css/update_customer.css">
</head>
<body>
<% if (customer != null) { %>
    <form action="update_process.jsp" method="post" onsubmit="return validateForm()">
        <input type="hidden" name="account_no" value="<%= customer.getAccountNo() %>" />
        <label for="fullName">Full Name:</label>
        <input type="text" id="fullName" name="full_name" value="<%= customer.getFullName() %>" /><br />

        <label for="address">Address:</label>
        <input type="text" id="address" name="address" value="<%= customer.getAddress() %>" /><br />

        <label for="mobileNo">Mobile No:</label>
        <input type="text" id="mobileNo" name="mobile_no" value="<%= customer.getMobileNo() %>" /><br />

        <label for="email">Email ID:</label>
        <input type="text" id="email" name="email" value="<%= customer.getEmail() %>" /><br />

        <label for="accountType">Account Type:</label>
        <input type="text" id="accountType" name="account_type" value="<%= customer.getAccountType() %>" /><br />

        <label>Date of Birth:</label>
        <input type="text" id="dob" name="dob" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(customer.getDob()) %>" /><br />

        <div style="display: flex; " class="radio-group">
            <label class="radio-label">ID Proof:</label>
            <input type="radio" id="aadhar" name="id_proof" value="Aadhar" <%= customer.getIdProof().equals("Aadhar") ? "checked" : "" %> />
            <label for="aadhar">Aadhar</label>
            <input type="radio" id="pan" name="id_proof" value="PAN" <%= customer.getIdProof().equals("PAN") ? "checked" : "" %> />
            <label for="pan">PAN</label>
        </div>

        <input type="submit" value="Update Customer" />
    </form>
<% } else { %>
    <p>Customer not found. Please try again.</p>
<% } %>
</body>
</html>
