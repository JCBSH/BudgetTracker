package com.mycompany.btrack.models;

import com.mycompany.btrack.models.Transaction;

import java.util.Date;
import java.util.UUID;

/**
 * Created by godfreytruong on 11/10/2015.
 */
public class TransactionDB {
    private UUID id;
    private String recipient;
    private double amount;
    private Date date;
    private String description;
    private String category;
    private String priority;

    public TransactionDB() {
    }

    public TransactionDB(Transaction transaction) {
        this.id = transaction.getId();
        this.recipient = transaction.getRecipient();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
        this.category = transaction.getCategory();
        this.priority = transaction.getPriority();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
