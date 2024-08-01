// Transaction.java
package bank;

import java.util.Date;

public class Transaction {
    private int transactionId;
    private int accountNo;
    private String transactionType;
    private double amount;
    private Date transactionDate;

    // Getters and setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getAccountNo() { return accountNo; }
    public void setAccountNo(int accountNo) { this.accountNo = accountNo; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }
}
