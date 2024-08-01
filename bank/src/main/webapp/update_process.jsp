<%@ page import="bank.Customer" %>
<%@ page import="bank.CustomerDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
response.setHeader("Cache-Control", "no-store");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0"); // Prevents caching at the proxy server

String accountNo = request.getParameter("account_no");
String fullName = request.getParameter("full_name");
String address = request.getParameter("address");
String mobileNo = request.getParameter("mobile_no");
String emailId = request.getParameter("email");
String accountType = request.getParameter("account_type");
String dobStr = request.getParameter("dob");
String idProof = request.getParameter("id_proof");

// Parse the date from String to java.util.Date
java.util.Date utilDate = null;
try {
    utilDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
} catch (java.text.ParseException e) {
    e.printStackTrace();
    response.sendRedirect("error.jsp"); // Redirect to an error page if date parsing fails
}

// Convert java.util.Date to java.sql.Date
java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

Customer customer = new Customer();
customer.setAccountNo(Integer.parseInt(accountNo));
customer.setFullName(fullName);
customer.setAddress(address);
customer.setMobileNo(mobileNo);
customer.setEmail(emailId);
customer.setAccountType(accountType);
customer.setDob(sqlDate); // Set the sqlDate here
customer.setIdProof(idProof);

try {
    boolean isUpdated = CustomerDAO.updateCustomer(customer);

    if (isUpdated) {
        response.sendRedirect("admin_dashboard.jsp?status=updateSuccess");
    } else {
        out.println("<h2>Unable to update customer details. Please try again.</h2>");
    }
} catch (Exception e) {
    e.printStackTrace();
    out.println("<h2>Error in updating the customer details:</h2> " + e.getMessage());
}
%>
