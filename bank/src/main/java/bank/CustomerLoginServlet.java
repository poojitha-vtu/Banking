package bank;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CustomerLoginServlet")
public class CustomerLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNo = request.getParameter("accountNo");
        String password = request.getParameter("password");
        
        System.out.println("Attempting to login with Account No: " + accountNo);
        
        try {
            // Get database connection
            try (Connection conn = DatabaseConnection.getConnection();
                 // Check customer credentials
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM customer WHERE account_no=? AND password=?")) {
                
                pstmt.setString(1, accountNo);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Customer login successful, retrieve initial balance and first_login status
                        int initialBalance = rs.getInt("initial_balance");
                        int firstLogin = rs.getInt("first_login");
                        
                        System.out.println("Login successful for Account No: " + accountNo + ", Initial Balance: " + initialBalance + ", First Login: " + firstLogin);
                        
                        // Set the initial balance as a session attribute
                        HttpSession session = request.getSession();
                        int timeoutInSeconds = 1800; // 30 minutes (you can set the desired timeout value)
                        session.setMaxInactiveInterval(timeoutInSeconds);
                        session.setAttribute("accountNo", Integer.parseInt(accountNo)); // Convert to Integer
                        session.setAttribute("initialBalance", initialBalance);
                        
                        if (firstLogin == 1) {
                            // Update first_login to 1
                            try (PreparedStatement updatePstmt = conn.prepareStatement("UPDATE customer SET first_login = 0 WHERE account_no = ?")) {
                                updatePstmt.setString(1, accountNo);
                                updatePstmt.executeUpdate();
                            }
                            
                            // Redirect to dashboard
                            response.sendRedirect("change_password.jsp");
                        } else {
                            // Redirect to change password page
                            response.sendRedirect("customer_dashboard.jsp");
                        }
                    } else {
                        // Customer login failed, redirect back to login page with error message
                        System.out.println("Login failed for Account No: " + accountNo);
                        response.sendRedirect("customerlogin.jsp?error=1");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
