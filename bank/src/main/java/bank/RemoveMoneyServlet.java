package bank;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RemoveMoneyServlet")
public class RemoveMoneyServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters from the request
        String amountToRemoveStr = request.getParameter("amountToRemove");
        String accountNoStr = request.getSession().getAttribute("accountNo").toString();
        int accountNo = Integer.parseInt(accountNoStr);
        double amountToRemove = Double.parseDouble(amountToRemoveStr);

        try {
            boolean updateSuccess = CustomerDAO.updateCustomerBalance(accountNo, -amountToRemove);
            boolean transactionSuccess = TransactionDAO.addTransaction(accountNo, "Withdrawal", amountToRemove);

            if (updateSuccess && transactionSuccess) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process the transaction.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred while processing the request.");
        }
    }
}
