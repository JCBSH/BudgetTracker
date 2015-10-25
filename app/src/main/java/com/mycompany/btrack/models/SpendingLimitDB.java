package com.mycompany.btrack.models;

import java.util.UUID;

/**
 * Created by Josephine Js on 10/21/2015.
 */
public class SpendingLimitDB {

    private double amount;

    public SpendingLimitDB() {
        // empty default constructor, necessary for Firebase to be able to deserialize the class
    }

    public double getAmount() {
        return amount;
    }

}
