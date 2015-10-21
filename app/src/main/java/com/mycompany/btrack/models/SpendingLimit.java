package com.mycompany.btrack.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * Created by Josephine Js on 10/21/2015.
 */
public class SpendingLimit {

    public static final  Double AMOUNT_DEFAULT = 0.00;
    private static final String JSON_ID = "id";
    private static final String JSON_AMOUNT = "amount";

    private static final String TAG = Transaction.class.getSimpleName();

    private UUID mId;
    private double mAmount;

    public SpendingLimit() {
        mId = UUID.randomUUID();
        mAmount = AMOUNT_DEFAULT;
    }

    public SpendingLimit(SpendingLimitDB limit) {
        mId = limit.getId();
        mAmount = limit.getAmount();
    }
    public SpendingLimit(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_AMOUNT)) {
            mAmount = json.getDouble(JSON_AMOUNT);
        }
    }

    public UUID getId() {
        return mId;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

}
