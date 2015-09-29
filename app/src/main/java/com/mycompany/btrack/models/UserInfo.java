package com.mycompany.btrack.models;

import android.content.Context;
import android.util.Log;

import com.mycompany.btrack.models.JSONParsers.TransactionJSONSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

/**
 * Created by JCBSH on 27/09/2015.
 */
public class UserInfo {
    private ArrayList<Transaction> mTransactions;
    private static UserInfo sUserInfo;
    private static int temp = 0;
    private Context mAppContext;

    private static final String TAG = UserInfo.class.getSimpleName();
    private static final String FILENAME = "crimes.json";
    private TransactionJSONSerializer mTransactionSerializer;

    public class TransactionComparator implements Comparator<Transaction> {
        @Override
        public int compare(Transaction o1, Transaction o2) {
            int i = o1.getDate().compareTo(o2.getDate());
            if (i == 1) {
                return -1;
            } else if (i == -1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private UserInfo(Context appContext) {
        mAppContext = appContext;
        mTransactionSerializer = new TransactionJSONSerializer(mAppContext,FILENAME);
        try {
            mTransactions = mTransactionSerializer.loadTransactions();
            Log.d(TAG, "load transaction");
            sortTransactions();

        } catch (Exception e) {
            mTransactions = new ArrayList<Transaction>();
            Log.e(TAG, "Error loading crimes: ", e);
        }
    }

    public static UserInfo get(Context c) {
        if (sUserInfo == null) {
            sUserInfo = new UserInfo(c.getApplicationContext());
        }
        if (temp == 0) {
            temp = 4;
        }
        return sUserInfo;
    }



    public boolean saveTransactions() {
        try {
            mTransactionSerializer.saveTransactions(mTransactions);
            int d = Log.d(TAG, "Transactions saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving Transactions: ", e);
            e.printStackTrace();
            return false;
        }
    }

    public void addTransaction(Transaction c) {
        mTransactions.add(c);
        sortTransactions();
    }

    public void deleteTransaction(Transaction c) {mTransactions.remove(c); }

    public ArrayList<Transaction> getTransactions() {
        return mTransactions;
    }

    public Transaction getTransaction(UUID uuid) {
        for (Transaction transaction:mTransactions) {
            if (transaction.getId().equals(uuid)) return transaction;
        }
        return null;
    }

    public void sortTransactions () {
        Collections.sort(mTransactions, new TransactionComparator());
    }
}
