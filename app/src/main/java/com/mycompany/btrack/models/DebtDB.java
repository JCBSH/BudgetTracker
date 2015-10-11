package com.mycompany.btrack.models;

import java.util.Date;
import java.util.UUID;

/**
 * Created by godfreytruong on 12/10/2015.
 */
public class DebtDB {
    private UUID id;
    private double amount;
    private Date date;
    private String description;

    public DebtDB() {
    }

    public DebtDB(Debt d) {
        this.id = d.getId();
        this.amount = d.getAmount();
        this.date = d.getDate();
        this.description = d.getDescription();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
