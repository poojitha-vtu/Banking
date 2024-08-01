<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.*, java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="bank.AdminDAO" %>

<%
    String adminUsername = request.getParameter("adminUsername");
    String adminPassword = request.getParameter("adminPassword");

    boolean adminLoggedIn = false;

    try {
        // Check admin credentials using AdminDAO class
        adminLoggedIn = AdminDAO.checkAdminCredentials(adminUsername, adminPassword);

        if (adminLoggedIn) {
            response.sendRedirect("admin_dashboard.jsp");
        } else {
            // Admin login failed, display error message
            out.println("<h2>Admin Login Failed!</h2>");
        }
    } catch (Exception e) {
        e.printStackTrace();
        out.println("<h2>Error occurred during admin login.</h2>");
    }
%>
