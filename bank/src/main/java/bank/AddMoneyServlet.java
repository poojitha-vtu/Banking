package bank;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/AddMoneyServlet")
public class AddMoneyServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters from the request
        String amountToAddStr = request.getParameter("amountToAdd");
        Integer accountNo = (Integer) request.getSession().getAttribute("accountNo"); // Correctly cast to Integer

        double amountToAdd;
        
        try {
            amountToAdd = Double.parseDouble(amountToAddStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid amount format.");
            return;
        }

        if (accountNo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account number not found in session.");
            return;
        }

        try {
            boolean balanceUpdated = CustomerDAO.updateCustomerBalance(accountNo, amountToAdd);
            boolean transactionInserted = TransactionDAO.addTransaction(accountNo, "Deposit", amountToAdd);

            if (balanceUpdated && transactionInserted) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating balance or recording transaction.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
