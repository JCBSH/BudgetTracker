package com.mycompany.btrack.models;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by JCBSH on 28/09/2015.
 */
public class Transaction {
    public static final  Double AMOUNT_DEFAULT = 0.00;
    public static final  String RECIPIENT_DEFAULT = "empty";
    public static final  String DESCRIPTION_DEFAULT = "no description";
    public static final int RECIPIENT_SIZE_LIMIT = 20;
    public static final int DESCRIPTION_SIZE_LIMIT = 50;
    private static final String JSON_ID = "id";
    private static final String JSON_RECIPIENT = "recipient";
    private static final String JSON_AMOUNT = "amount";
    private static final String JSON_DATE = "date";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_CATEGORY = "category";
    private static final String JSON_PRIORITY = "priority";

    private static final String FOOD = "Food";
    private static final String FOOD_ICON = "mipmap/food_icon";
    private static final String GROCERY = "Grocery";
    private static final String GROCERY_ICON = "mipmap/grocery_icon";
    private static final String UTILITY = "Utility";
    private static final String UTILITY_ICON = "mipmap/utility_icon";
    private static final String ENTERTAINMENT = "Entertainment";
    private static final String ENTERTAINMENT_ICON = "mipmap/entertainment_icon";
    private static final String TRANSPORT = "Transport";
    private static final String TRANSPORT_ICON = "mipmap/transport_icon";
    private static final String OTHER = "Other";
    private static final String OTHER_ICON = "mipmap/other_icon";
    private static final String LOW_PRIORITY = "Low";
    private static final String LOW_ICON = "mipmap/low_icon";
    private static final String MEDIUM_PRIORITY = "Medium";
    private static final String MEDIUM_ICON = "mipmap/medium_icon";
    private static final String HIGH_PRIORITY = "High";
    private static final String HIGH_ICON = "mipmap/high_icon";
    private static final String TAG = Transaction.class.getSimpleName();

    private UUID mId;
    private String mRecipient;
    private double mAmount;
    private Date mDate;
    private String mDescription;
    private String mCategory;
    private String mPriority;


    public Transaction() {
        mId = UUID.randomUUID();
        mDate = new Date();
        mAmount = AMOUNT_DEFAULT;
        mRecipient = RECIPIENT_DEFAULT;
        mDescription = DESCRIPTION_DEFAULT;
        mCategory =  OTHER;
        mPriority = LOW_PRIORITY;
    }

    public Transaction(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_RECIPIENT)) {
            mRecipient = json.getString(JSON_RECIPIENT);
        }

        if (json.has(JSON_AMOUNT)) {
            mAmount = json.getDouble(JSON_AMOUNT);
        }

        if (json.has(JSON_DATE)) {
            mDate = new Date(json.getLong(JSON_DATE));
        }

        if (json.has(JSON_DESCRIPTION)) {
            mDescription = json.getString(JSON_DESCRIPTION);
        }

        if (json.has(JSON_CATEGORY)) {
            mCategory = json.getString(JSON_CATEGORY);
        }

        if (json.has(JSON_PRIORITY)) {
            mPriority = json.getString(JSON_PRIORITY);
        }
    }

    public String getRecipient() {
        return mRecipient;
    }

    public String getEditTextRecipient() {
        if (mRecipient.equalsIgnoreCase(RECIPIENT_DEFAULT)) {
            return "";
        } else {
            return mRecipient;
        }
    }

    public void setRecipient(String recipient) {
        if (!recipient.equalsIgnoreCase("")) {
            mRecipient = recipient;
        }
    }



    public double getAmount() {
        return mAmount;
    }

    public String getFormattedAmount() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (mAmount == 0) {
            return "0.00";
        } else {
            String formatted = df.format(mAmount);
            return formatted;
        }
    }


    public void setAmount(double amount) {
        mAmount = amount;
    }


    public String getFormattedDate() {
        String date = (String) DateFormat.format("EEEE, MMM dd, yyyy, k:mm", getDate());
        return date;
    }

    public String getFormattedDate2() {
        String date = (String) DateFormat.format("EEEE, dd/MM/yy, k:mm", getDate());
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

    public String getEditTextDescription() {
        if (mDescription.equalsIgnoreCase(DESCRIPTION_DEFAULT)) {
            return "";
        } else {
            return mDescription;
        }
    }

    public void setDescription(String description) {
        if (!description.equalsIgnoreCase("")) {
            mDescription = description;
        }
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
        json.put(JSON_CATEGORY, mCategory);
        json.put(JSON_PRIORITY, mPriority);
        return json;
    }


    public static ArrayList<Transaction> filterTransactions(
            ArrayList<Transaction> transactions, Date filterFromDate, Date filterToDate,
            double filterAmountFrom, double filterAmountTo, String filterRecipient, String filterDescription) {


        ArrayList<Transaction> filteredList =  new ArrayList<Transaction>();
        for (Transaction t : transactions) {
            boolean testResult = true;
//            Log.d("TransactionFragment", "     blah");
            if (!((t.getDate().after(filterFromDate) || t.getDate().equals(filterFromDate))
                    && (t.getDate().before(filterToDate) || t.getDate().equals(filterToDate)))) {
//                Log.d("TransactionFragment", "testing by date failed");
                testResult = false;
            }

//            Log.d("TransactionFragment", "testing by amount");
//            Log.d("TransactionFragment", String.format("current amount %s", t.getAmount()));
//            Log.d("TransactionFragment", String.format("from %s: to %s", filterAmountFrom, filterAmountTo));
            if (!(t.getAmount() >= filterAmountFrom && t.getAmount() <= filterAmountTo)) {
//                Log.d("TransactionFragment", "testing by amount failed");
                testResult = false;
            }

            if (!filterRecipient.equalsIgnoreCase("")) {
                if (!(t.getRecipient().toLowerCase().startsWith(filterRecipient.toLowerCase()))) {
//                    Log.d("TransactionFragment", "testing by Recipient failed");
                    testResult = false;
                }
            }

            if (!filterDescription.equalsIgnoreCase("")) {
                if (!(t.getDescription().toLowerCase().contains(filterDescription.toLowerCase()))) {
//                    Log.d("TransactionFragment", "testing by Description failed");
                    testResult = false;
                }
            }

            if (testResult == true) {
                filteredList.add(t);
            }
        }
        return filteredList;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getCategoryIconResource() {
        switch (mCategory) {
            case FOOD:
                return FOOD_ICON;
            case UTILITY:
                return UTILITY_ICON;
            case ENTERTAINMENT:
                return ENTERTAINMENT_ICON;
            case GROCERY:
                return GROCERY_ICON;
            case TRANSPORT:
                return TRANSPORT_ICON;
            case OTHER:
                return OTHER_ICON;
            default:
                return OTHER_ICON;
        }
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getPriority() {
        return mPriority;
    }

    public String getPriorityIconResource() {
        switch (mPriority) {
            case LOW_PRIORITY:
                return LOW_ICON;
            case MEDIUM_PRIORITY:
                return MEDIUM_ICON;
            case HIGH_PRIORITY:
                return HIGH_ICON;
            default:
                return LOW_ICON;
        }
    }

    public void setPriority(String priority) {
        mPriority = priority;
    }
}
