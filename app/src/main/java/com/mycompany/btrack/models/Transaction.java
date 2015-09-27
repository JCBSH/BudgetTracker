package com.mycompany.btrack.models;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by JCBSH on 28/09/2015.
 */
public class Transaction {
    private static final String JSON_ID = "id";
    private static final String JSON_RECIPIENT = "recipient";
    private static final String JSON_AMOUNT = "amount";
    private static final String JSON_DATE = "date";
    private static final String JSON_DESCRIPTION = "description";

    private static final String TAG = Transaction.class.getSimpleName();

    private UUID mId;
    private String mRecipient;
    private double mAmount;
    private Date mDate;
    private String mDescription;


    public Transaction() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Transaction(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_RECIPIENT)) {
            mRecipient = json.getString(JSON_RECIPIENT);
        }
        if (json.has(JSON_AMOUNT)) {
            mAmount = json.getDouble(JSON_AMOUNT);
        }
        if (json.has(JSON_RECIPIENT)) {
            mDate = new Date(json.getLong(JSON_DATE));
        }

        if (json.has(JSON_DESCRIPTION)) {
            mDescription = json.getString(JSON_DESCRIPTION);
        }
    }

    public String getRecipient() {
        return mRecipient;
    }

    public void setRecipient(String recipient) {
        mRecipient = recipient;
    }



    public double getAmount() {
        return mAmount;
    }

    public double getFormattedAmount() {
        return mAmount;
    }


    public void setAmount(double amount) {
        mAmount = amount;
    }


    public String getFormattedDate() {
        String date = (String) DateFormat.format("EEEE, MMM dd, yyyy, h:mm a", getDate());
        return date;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {

        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }


    public UUID getId() {
        return mId;
    }

    @Override
    public String toString() {
        return mRecipient;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_RECIPIENT, mRecipient);
        //Log.d(TAG, mTitle);
        json.put(JSON_AMOUNT, mAmount);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_DESCRIPTION, mDescription);
        return json;
    }
}
