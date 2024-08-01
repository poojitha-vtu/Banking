package bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String SELECT_ALL_CUSTOMERS_QUERY = "SELECT * FROM customer";
    private static final String SELECT_CUSTOMER_BY_ACCOUNT_NO_QUERY = "SELECT * FROM customer WHERE account_no = ?";
    private static final String DELETE_CUSTOMER_QUERY = "DELETE FROM customer WHERE account_no = ?";
    private static final String UPDATE_CUSTOMER_QUERY = "UPDATE customer SET full_name=?, address=?, mobile_no=?, email=?, account_type=?, initial_balance=?, dob=?, id_proof=?, password=?, first_login=? WHERE account_no=?";
    private static final String INSERT_CUSTOMER_QUERY = 
            "INSERT INTO customer (full_name, address, mobile_no, email, account_type, initial_balance, dob, id_proof, account_no, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        private static final String INSERT_INITIAL_TRANSACTION_QUERY = 
            "INSERT INTO transaction (account_no, transaction_type, amount, transaction_date) VALUES (?, ?, ?, ?)";

    public static List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_CUSTOMERS_QUERY);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setAccountNo(rs.getInt("account_no"));
                customer.setFullName(rs.getString("full_name"));
                customer.setAddress(rs.getString("address"));
                customer.setMobileNo(rs.getString("mobile_no"));
                customer.setEmail(rs.getString("email"));
                customer.setAccountType(rs.getString("account_type"));
                customer.setInitialBalance(rs.getDouble("initial_balance")); // Add this line
                customer.setDob(rs.getDate("dob"));
                customer.setIdProof(rs.getString("id_proof"));
                customer.setPassword(rs.getString("password"));
                customer.setFirstLogin(rs.getBoolean("first_login"));
                customers.add(customer);
            }
        }
        return customers;
    }

    public static Customer getCustomerByAccountNo(int accountNo) throws SQLException {
        Customer customer = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_ACCOUNT_NO_QUERY)) {
             
            pstmt.setInt(1, accountNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setAccountNo(rs.getInt("account_no"));
                    customer.setFullName(rs.getString("full_name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setMobileNo(rs.getString("mobile_no"));
                    customer.setEmail(rs.getString("email"));
                    customer.setAccountType(rs.getString("account_type"));
                    customer.setInitialBalance(rs.getDouble("initial_balance")); // Add this line
                    customer.setDob(rs.getDate("dob"));
                    customer.setIdProof(rs.getString("id_proof"));
                    customer.setPassword(rs.getString("password"));
                    customer.setFirstLogin(rs.getBoolean("first_login"));
                }
            }
        }
        return customer;
    }

    public static boolean updateCustomer(Customer customer) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE customer SET full_name=?, address=?, mobile_no=?, email=?, account_type=?, dob=?, id_proof=?");

        // Add the password field to the query only if it's not null or empty
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            query.append(", password=?");
        }

        query.append(" WHERE account_no=?");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {

            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getMobileNo());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getAccountType());
            pstmt.setDate(6, new java.sql.Date(customer.getDob().getTime())); // Convert java.util.Date to java.sql.Date
            pstmt.setString(7, customer.getIdProof());

            // Set the password parameter only if it's not null or empty
            int index = 8;
            if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
                pstmt.setString(index++, customer.getPassword());
            }

            pstmt.setInt(index, customer.getAccountNo());

            int updated = pstmt.executeUpdate();
            return updated > 0;
        }
    }
    public static boolean deleteCustomerByAccountNo(String accountNo) throws SQLException {
        String query = "DELETE FROM customer WHERE account_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, accountNo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public static boolean updateCustomerBalance(int accountNo, double amountToAdd) throws SQLException {
        String query = "UPDATE customer SET initial_balance = initial_balance + ? WHERE account_no = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, amountToAdd);
            pstmt.setInt(2, accountNo); // Use setInt for Integer
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }
    public static boolean updateCustomerPassword(int accountNo, String newPassword) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        boolean isUpdated = false;

        try {
            // Get a database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to update the customer's password
            String sql = "UPDATE customer SET password = ? WHERE account_no = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, accountNo);

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = (rowsAffected > 0);

        } finally {
            // Close resources
            if (pstmt != null) pstmt.close();
            if (connection != null) connection.close();
        }

        return isUpdated;
    }
    public static double getCustomerBalance(int accountNo) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double balance = 0;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT initial_balance FROM customer WHERE account_no = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountNo);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("initial_balance");
            }

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (connection != null) connection.close();
        }

        return balance;
    }
    public static boolean deleteCustomer(int accountNo) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        boolean isDeleted = false;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "DELETE FROM customer WHERE account_no = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountNo);

            int rowsAffected = pstmt.executeUpdate();
            isDeleted = (rowsAffected > 0);

        } finally {
            if (pstmt != null) pstmt.close();
            if (connection != null) connection.close();
        }

        return isDeleted;
    }
    public static boolean validateCustomerCredentials(String accountNo, String password) throws SQLException {
        String query = "SELECT * FROM customer WHERE account_no=? AND password=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, accountNo);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if a record is found
            }
        }
    }

    public static Customer getCustomerDetails(int accountNo) throws SQLException {
        String query = "SELECT * FROM customer WHERE account_no=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, accountNo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Populate Customer object with details from ResultSet
                    Customer customer = new Customer();
                    customer.setAccountNo(rs.getInt("account_no"));
                    customer.setFullName(rs.getString("full_name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setMobileNo(rs.getString("mobile_no"));
                    customer.setEmail(rs.getString("email"));
                    customer.setAccountType(rs.getString("account_type"));
                    customer.setInitialBalance(rs.getDouble("initial_balance")); // Map initial balance
                    customer.setDob(rs.getDate("dob"));
                    customer.setIdProof(rs.getString("id_proof"));
                    customer.setPassword(rs.getString("password"));
                    customer.setFirstLogin(rs.getBoolean("first_login"));
                    
                    return customer;
                }
                return null; // No customer found
            }
        }
    }

    public static void updateFirstLogin(String accountNo) throws SQLException {
        String query = "UPDATE customer SET first_login = 0 WHERE account_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, accountNo);
            pstmt.executeUpdate();
        }
    }
    public boolean registerCustomer(Customer customer) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_CUSTOMER_QUERY)) {
             
            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getMobileNo());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getAccountType());
            pstmt.setDouble(6, customer.getInitialBalance());
            pstmt.setDate(7, new java.sql.Date(customer.getDob().getTime()));
            pstmt.setString(8, customer.getIdProof());
            pstmt.setInt(9, customer.getAccountNo());
            pstmt.setString(10, customer.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public boolean insertInitialTransaction(int accountNo, double initialBalance) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_INITIAL_TRANSACTION_QUERY)) {
             
            pstmt.setInt(1, accountNo);
            pstmt.setString(2, "Deposit"); // Assuming account creation is a deposit
            pstmt.setDouble(3, initialBalance);
            pstmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    }

