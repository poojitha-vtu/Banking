// TransactionDAO.java
package bank;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Method to get recent transactions
    public static List<Transaction> getRecentTransactions(String accountNo) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transaction WHERE account_no = ? ORDER BY transaction_date DESC LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, accountNo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setAccountNo(rs.getInt("account_no"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setTransactionDate(rs.getDate("transaction_date"));
                transactions.add(transaction);
            }
        }

        return transactions;
    }
    public static boolean addTransaction(int accountNo, String transactionType, double amount) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        boolean isInserted = false;

        try {
            // Get a database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to insert a new transaction
            String sql = "INSERT INTO transaction (account_no, transaction_type, amount, transaction_date) VALUES (?, ?, ?, ?)";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountNo);
            pstmt.setString(2, transactionType);
            pstmt.setDouble(3, amount);
            pstmt.setDate(4, Date.valueOf(LocalDateTime.now().toLocalDate())); // Insert the current date

            // Execute the insert
            int rowsAffected = pstmt.executeUpdate();
            isInserted = (rowsAffected > 0);

        } finally {
            // Close resources
            if (pstmt != null) pstmt.close();
            if (connection != null) connection.close();
        }

        return isInserted;
    }
}
