package bank;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CustomerRegistrationServlet")
public class CustomerRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDAO customerDAO = new CustomerDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String mobileNo = request.getParameter("mobileNo");
        String email = request.getParameter("email");
        String accountType = request.getParameter("accountType");
        double initialBalance = Double.parseDouble(request.getParameter("initialBalance"));
        String dobString = request.getParameter("dob");
        String idProof = request.getParameter("idProof");
       
        
        


        // Generate account number and temporary password
        int accountNo = generateAccountNumber();
        String temporaryPassword = generateTemporaryPassword();
        

  

        // Parse date
        Date dob = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Adjust date format as needed
            dob = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid date format");
            request.getRequestDispatcher("registration_error.jsp").forward(request, response);
            return;
        }
        java.sql.Date sqlDob = new java.sql.Date(dob.getTime());

        // Create Customer object
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setAddress(address);
        customer.setMobileNo(mobileNo);
        customer.setEmail(email);
        customer.setAccountType(accountType);
        customer.setInitialBalance(initialBalance);
        customer.setDob(sqlDob);
        customer.setIdProof(idProof);
        customer.setAccountNo(accountNo);
        customer.setPassword(temporaryPassword);
        customer.setFirstLogin(true); // Set first login status

        try {
            // Register the customer using DAO
            boolean success = customerDAO.registerCustomer(customer);
            if (success) {
                // Insert initial transaction record
                boolean transactionSuccess = customerDAO.insertInitialTransaction(accountNo, initialBalance);

                if (transactionSuccess) {
                    // Store account number in session
                    HttpSession session = request.getSession();
                    session.setAttribute("account_no", accountNo);

                    // Redirect to registration success page
                    response.sendRedirect("registration_success.jsp?accountNo=" + accountNo + "&password=" + temporaryPassword);
                } else {
                    // Redirect to registration error page
                    response.sendRedirect("registration_error.jsp");
                }
            } else {
                // Redirect to registration error page
                response.sendRedirect("registration_error.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Redirect to error page
            response.sendRedirect("error.jsp");
        }
    }

    private int generateAccountNumber() {
        // Generate random account number
        return (int) (Math.random() * 900000) + 100000; // Generates a 6-digit number
    }

    private String generateTemporaryPassword() {
        // Generate random temporary password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
 

}