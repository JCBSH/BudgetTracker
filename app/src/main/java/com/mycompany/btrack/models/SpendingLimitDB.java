package com.mycompany.btrack.models;

import java.util.UUID;

/**
 * Created by Josephine Js on 10/21/2015.
 */
public class SpendingLimitDB {

    private UUID id;
    private double amount;

    public SpendingLimitDB(SpendingLimit limit) {
        this.id = limit.getId();
        this.amount = limit.getAmount();
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

}
