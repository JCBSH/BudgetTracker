package com.mycompany.btrack.models;

/**
 * Created by Josephine Js on 10/21/2015.
 */
public class SpendingLimitDB {

    private double amount;

    public SpendingLimitDB() {
        
    }

    public SpendingLimitDB(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

}
