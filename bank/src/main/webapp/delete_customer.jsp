<%@ page import="java.sql.*" %>
<%@ page import="bank.DatabaseConnection" %>
<%@ page import="bank.CustomerDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
response.setHeader("Cache-Control","no-store");
response.setHeader("Pragma","no-cache"); 
response.setHeader("Expires", "0"); // Prevents caching at the proxy server

String accountNo = request.getParameter("account_no");

if (accountNo != null && !accountNo.isEmpty()) {
    try {
        boolean isDeleted = CustomerDAO.deleteCustomerByAccountNo(accountNo);
        if (isDeleted) {
            response.sendRedirect("admin_dashboard.jsp?status=deleteSuccess");
        } else {
            out.println("<h2>Error in deleting the customer. Please try again.</h2>");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        out.println("<h2>Error while deleting the customer:</h2> " + e.getMessage());
    }
} else {
    out.println("<h2>Invalid account number provided.</h2>");
}
%>
