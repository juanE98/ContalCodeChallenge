package net.contal.demo.modal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class BankTransaction {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_account_id", nullable = false)
    @JsonBackReference
    private CustomerAccount customerAccount;

    @Column(name = "transactionAmount", nullable = false)
    private double transactionAmount;

    @Column(name = "transactionDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}