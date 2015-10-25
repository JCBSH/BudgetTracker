package com.mycompany.btrack.models;

import android.text.format.DateFormat;

import com.mycompany.btrack.utils.MoneyTextWatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Josephine Js on 10/21/2015.
 */
public class SpendingLimit {

    private ArrayList<Transaction> mTransactions;
    private double mLimit;
    private double mBalanceAfterLimit;
    private boolean overStatus;
    private String mMonth;



    public SpendingLimit(ArrayList<Transaction> transactions) {
        mLimit = 0.00;
        mTransactions = transactions;
        mBalanceAfterLimit = 0.0;
        overStatus =  false;
        Date curDate = new Date();
        mMonth = (String) DateFormat.format("MMM", curDate);
        update();
    }

    public SpendingLimit(double limit, ArrayList<Transaction> transactions) {
        mLimit = limit;
        mTransactions = transactions;
        mBalanceAfterLimit = 0.0;
        overStatus =  false;
        Date curDate = new Date();
        mMonth = (String) DateFormat.format("MMM", curDate);
        update();
    }

    public double getAmount() {
        return mLimit;
    }

    public double getLimit() {
        return mLimit;
    }

    public void setLimit(double d) {
        this.mLimit = d;

        update();
    }

    public boolean overStatus() {
        return overStatus;
    }

    @Override
    public String toString() {
        String statusTitle = "Under";
        if (overStatus) {
            statusTitle = "Over";
        }
        return (mMonth + " Limit : " + mLimit + " | " + statusTitle + ": " + mBalanceAfterLimit);
    }

    public void update() {

        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Date fromDate = new GregorianCalendar(year, month, 1, 0, 0, 0).getTime();
        Date toDate = new GregorianCalendar(year, month, daysInMonth, 23,59, 59).getTime();

        ArrayList<Transaction> thisMonthTransactions = Transaction.filterTransactions(mTransactions,
                fromDate, toDate,
                MoneyTextWatcher.MIN_AMOUNT_LIMIT, MoneyTextWatcher.MAX_AMOUNT_LIMIT,
                "", "",
                Transaction.ALL_CATEGORY, Transaction.ALL_PRIORITY);



        Date curDate = new Date();
        mMonth = (String) DateFormat.format("MMM", curDate);


        mBalanceAfterLimit = Transaction.getTotalAmount(thisMonthTransactions);


        if (mBalanceAfterLimit > mLimit) {
            overStatus = true;
        } else {
            overStatus = false;
        }


    }
}
