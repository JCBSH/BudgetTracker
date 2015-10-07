package com.mycompany.btrack.savedStates;

import android.content.Context;

import com.mycompany.btrack.models.Transaction;

import java.util.Date;

/**
 * Created by JCBSH on 7/10/2015.
 */
public class TransactionFragmentState {


    private boolean mFilterStatus;
    private Date mFilterFromDate;
    private Date mFilterToDate;
    private double mFilterAmountFrom;
    private double mFilterAmountTo;
    private String mFilterRecipient;
    private String mFilterDescription;
    private String mFilterCategory;
    private String mFilterPriority;
    private Context mAppContext;
    private static TransactionFragmentState sTransactionFragmentState;

    private TransactionFragmentState(Context appContext) {
        mAppContext = appContext;

        mFilterStatus = false;
        mFilterFromDate = new Date();
        mFilterToDate = new Date();
        mFilterAmountFrom = 0;
        mFilterAmountTo = 0;
        mFilterRecipient = "";
        mFilterDescription = "";
        mFilterCategory = Transaction.ALL_CATEGORY;
        mFilterPriority = Transaction.ALL_PRIORITY;
    }

    public static TransactionFragmentState get(Context c) {
        if (sTransactionFragmentState == null) {
            sTransactionFragmentState = new TransactionFragmentState(c.getApplicationContext());
        }
        return sTransactionFragmentState;
    }

    public boolean isFilterStatus() {
        return mFilterStatus;
    }

    public void setFilterStatus(boolean filterStatus) {
        mFilterStatus = filterStatus;
    }

    public Date getFilterFromDate() {
        return mFilterFromDate;
    }

    public void setFilterFromDate(Date filterFromDate) {
        mFilterFromDate = filterFromDate;
    }

    public Date getFilterToDate() {
        return mFilterToDate;
    }

    public void setFilterToDate(Date filterToDate) {
        mFilterToDate = filterToDate;
    }

    public double getFilterAmountFrom() {
        return mFilterAmountFrom;
    }

    public void setFilterAmountFrom(double filterAmountFrom) {
        mFilterAmountFrom = filterAmountFrom;
    }

    public double getFilterAmountTo() {
        return mFilterAmountTo;
    }

    public void setFilterAmountTo(double filterAmountTo) {
        mFilterAmountTo = filterAmountTo;
    }

    public String getFilterRecipient() {
        return mFilterRecipient;
    }

    public void setFilterRecipient(String filterRecipient) {
        mFilterRecipient = filterRecipient;
    }

    public String getFilterDescription() {
        return mFilterDescription;
    }

    public void setFilterDescription(String filterDescription) {
        mFilterDescription = filterDescription;
    }

    public String getFilterCategory() {
        return mFilterCategory;
    }

    public void setFilterCategory(String filterCategory) {
        mFilterCategory = filterCategory;
    }

    public String getFilterPriority() {
        return mFilterPriority;
    }

    public void setFilterPriority(String filterPriority) {
        mFilterPriority = filterPriority;
    }
}
