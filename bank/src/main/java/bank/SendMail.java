package bank;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SendMail")
public class SendMail extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String recipientEmail = request.getParameter("email");
        String accountNo = request.getParameter("accountNo");
        String tempPassword = request.getParameter("password");

        System.out.println("Recipient Email: " + recipientEmail);
        System.out.println("Account Number: " + accountNo);
        System.out.println("Temporary Password: " + tempPassword);

        // Rest of your email sending logic here...
    }
}
