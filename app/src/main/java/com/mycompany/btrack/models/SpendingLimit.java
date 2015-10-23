package com.mycompany.btrack.models;

import java.util.UUID;

/**
 * Created by Josephine Js on 10/21/2015.
 */
public class SpendingLimit {

    private double mAmount;

    public SpendingLimit() {

    }

    public SpendingLimit(double limit) {
        mAmount = limit;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double d) {
        this.mAmount = d;
    }

}
