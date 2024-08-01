package bank;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/deleteAccount")
public class DeleteAccountServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNoStr = request.getParameter("accountNo");

        if (accountNoStr == null || accountNoStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Account number is required.");
            return;
        }

        int accountNo;
        try {
            accountNo = Integer.parseInt(accountNoStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid account number format.");
            return;
        }

        HttpSession session = request.getSession(false); // Get the session without creating a new one
        if (session != null) {
            session.invalidate(); // Invalidate the current session
        }

        try {
            double balance = CustomerDAO.getCustomerBalance(accountNo);

            if (balance == 0) {
                boolean deleted = CustomerDAO.deleteCustomer(accountNo);
                if (deleted) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("Failed to delete account.");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().println("Cannot delete account. Account balance is not zero.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error occurred while processing the request.");
        }
    }
}
