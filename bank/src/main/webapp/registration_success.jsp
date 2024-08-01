<%@ page import="java.util.Properties" %>
<%@ page import="javax.mail.*" %>
<%@ page import="javax.mail.internet.*" %>

<%
response.setHeader("Cache-Control","no-store");
response.setHeader("Pragma","no-cache");
response.setHeader("Expires", "0"); //prevents caching at the proxy server
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Success</title>
    <link rel="stylesheet" type="text/css" href="./css/registration_success.css">
    <style>
        .back-button {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin: 5px;
        }

        .back-button:hover {
            background-color: #45a049;
        }
    </style>
    <script>
        function redirectToAdminDashboard() {
            window.location.href = "admin_dashboard.jsp";
        }

        function sendMail() {
            document.getElementById("emailForm").submit();
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Registration Successful</h2>
        <p>Your account has been successfully registered!</p>
        <p id="AC">Account Number: <%= request.getParameter("accountNo") %></p>
        <p id="temp_pass">Temporary Password: <%= request.getParameter("password") %></p>
        <button class="back-button" onclick="redirectToAdminDashboard()">Go Back to Admin Dashboard</button>
        <button class="back-button" onclick="sendMail()">Send Email</button>
    </div>

    <form id="emailForm" action="" method="post" style="display: none;">
        <input type="hidden" name="sendEmail" value="true">
    </form>

    <%
    if ("POST".equalsIgnoreCase(request.getMethod()) && "true".equals(request.getParameter("sendEmail"))) {
        final String username = "poojitha7857@gmail.com"; // Your email
        final String password = "fcgt wffm xhyr kire"; // Your email password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session emailSession = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(emailSession);
            message.setFrom(new InternetAddress(username));
            // Use the email from the registration form
            String recipientEmail = request.getParameter("email");
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Account Registration Details");
            message.setText("Hi. Hope you are doing well. Your account details are:\n\n" +
                            "Account Number: " + request.getParameter("accountNo") + "\n" +
                            "Temporary Password: " + request.getParameter("password"));

            Transport.send(message);
            out.println("<script>alert('Email sent successfully!');</script>");
        } catch (MessagingException e) {
            out.println("<script>alert('Error sending email: " + e.getMessage() + "');</script>");
        }
    }
    %>
</body>
</html>
