package bank;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/GetTransactionDetailsServlet")
public class GetTransactionDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer accountNo = (Integer) request.getSession().getAttribute("accountNo");

        if (accountNo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No account number found in session.");
            return;
        }

        String accountNoStr = accountNo.toString();

        try {
            // Retrieve transactions using TransactionDAO
            List<Transaction> transactions = TransactionDAO.getRecentTransactions(accountNoStr);

            // Construct JSON array and send response
            StringBuilder json = new StringBuilder("[");
            for (Transaction transaction : transactions) {
                json.append("{")
                    .append("\"transactionId\": ").append(transaction.getTransactionId()).append(",")
                    .append("\"accountNo\": \"").append(transaction.getAccountNo()).append("\",")
                    .append("\"transactionType\": \"").append(transaction.getTransactionType()).append("\",")
                    .append("\"amount\": ").append(transaction.getAmount()).append(",")
                    .append("\"transactionDate\": \"").append(transaction.getTransactionDate()).append("\"")
                    .append("},");
            }
            if (!transactions.isEmpty()) {
                json.deleteCharAt(json.length() - 1); // Remove trailing comma
            }
            json.append("]");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());

        } catch (SQLException e) {
            e.printStackTrace(); // Log to console or file
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred while fetching transaction details.");
        }
    }
}
