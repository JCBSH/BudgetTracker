package com.mycompany.btrack.models;

import android.text.format.DateFormat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by JCBSH on 2/10/2015.
 */
public class Debt {
    public static final  Double AMOUNT_DEFAULT = 0.00;
    public static final  String DESCRIPTION_DEFAULT = "no description";
    public static final int DESCRIPTION_SIZE_LIMIT = 50;

    private static final String TAG = Debt.class.getSimpleName();

    private UUID mId;
    private double mAmount;
    private Date mDate;
    private String mDescription;


    public Debt() {
        mId = UUID.randomUUID();
        mDate = new Date();
        mAmount = AMOUNT_DEFAULT;
        mDescription = DESCRIPTION_DEFAULT;
    }

    public Debt(DebtDB d) {
        mId = d.getId();
        mDate = d.getDate();
        mAmount = d.getAmount();
        mDescription = d.getDescription();
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
        return String.valueOf(mAmount);
    }



    public static ArrayList<Debt> filterDebts(
            ArrayList<Debt> debts, Date filterFromDate, Date filterToDate,
            double filterAmountFrom, double filterAmountTo, String filterRecipient, String filterDescription) {


        ArrayList<Debt> filteredList =  new ArrayList<Debt>();
        for (Debt t : debts) {
            boolean testResult = true;
            if (!((t.getDate().after(filterFromDate) || t.getDate().equals(filterFromDate))
                    && (t.getDate().before(filterToDate) || t.getDate().equals(filterToDate)))) {
                testResult = false;
            }
            if (!(t.getAmount() >= filterAmountFrom && t.getAmount() <= filterAmountTo)) {
                testResult = false;
            }

            if (!filterDescription.equalsIgnoreCase("")) {
                if (!(t.getDescription().toLowerCase().contains(filterDescription.toLowerCase()))) {
                    testResult = false;
                }
            }

            if (testResult == true) {
                filteredList.add(t);
            }
        }
        return filteredList;
    }

}
