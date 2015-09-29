package com.mycompany.btrack.models;

import android.content.Context;
import android.util.Log;

import com.mycompany.btrack.models.JSONParsers.DebtorJSONSerializer;
import com.mycompany.btrack.models.JSONParsers.TransactionJSONSerializer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

/**
 * Created by JCBSH on 27/09/2015.
 */
public class UserInfo {

    private JSONObject mJsonObject;
    private ArrayList<Transaction> mTransactions;
    private ArrayList<Debtor> mDebtors;
    private static UserInfo sUserInfo;
    private Context mAppContext;

    public static final String JSON_TRANSACTIONS = "transactions";
    public static final String JSON_DEBTORS = "debtors";
    public static final String JSON_DEBTOR_COUNT = "debtor_count";

    private static final String TAG = UserInfo.class.getSimpleName();
    private static final String FILENAME = "crimes.json";
    private TransactionJSONSerializer mTransactionSerializer;
    private DebtorJSONSerializer mDebtorSerializer;


    private UserInfo(Context appContext) {
        mAppContext = appContext;
        mTransactionSerializer = new TransactionJSONSerializer();
        mDebtorSerializer = new DebtorJSONSerializer();
        try {
            BufferedReader reader = null;
            try {
                InputStream in = mAppContext.openFileInput(FILENAME);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    // Line breaks are omitted and irrelevant
                    jsonString.append(line);
                }

                mJsonObject = new JSONObject(jsonString.toString());
                Log.d(TAG, mJsonObject.toString());


                JSONArray transactionsJSONArray = mJsonObject.getJSONArray(UserInfo.JSON_TRANSACTIONS);
                mTransactions = mTransactionSerializer.loadTransactions(transactionsJSONArray);

                JSONArray debtorsJSONArray = mJsonObject.getJSONArray(UserInfo.JSON_DEBTORS);
                mDebtors = mDebtorSerializer.loadDebtors(debtorsJSONArray);

                int debtorsCount = mJsonObject.getInt(JSON_DEBTOR_COUNT);
                Debtor.setCount(debtorsCount);

                Log.d(TAG, "successfully load transaction");
                sortTransactions();

            } catch (Exception e) {
                mTransactions = new ArrayList<Transaction>();
                mDebtors = new ArrayList<Debtor>();
                Log.e(TAG, "Error loading UserInfo: ", e);
                e.printStackTrace();
            } finally {
                if (reader != null)
                    reader.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "reader closing error: ", e);
            e.printStackTrace();
        }
    }

    public static UserInfo get(Context c) {
        if (sUserInfo == null) {
            sUserInfo = new UserInfo(c.getApplicationContext());
        }
        return sUserInfo;
    }



    public boolean saveTransactions() {
        try {
            JSONArray transactionsJsonArray = mTransactionSerializer.createJSONTransactions(mTransactions);
            mJsonObject.put(JSON_TRANSACTIONS, transactionsJsonArray);
            int d = Log.d(TAG, "Transactions saved to JSONObject");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving Transactions: ", e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveDebtors() {
        try {
            JSONArray debtorsJsonArray = mDebtorSerializer.createJSONDebtors(mDebtors);
            mJsonObject.put(JSON_DEBTORS, debtorsJsonArray);

            mJsonObject.put(JSON_DEBTOR_COUNT, Debtor.getCount());

            int d = Log.d(TAG, "Debtors saved to JSONObject");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving Debtors: ", e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveUserInfo() {
        try {
            Writer writer = null;
            try {
                OutputStream out = mAppContext
                        .openFileOutput(FILENAME, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(mJsonObject.toString());
                int d = Log.d(TAG, "UserInfo saved to file");
            } catch (Exception e) {
                Log.e(TAG, "Error saving UserInfo: ", e);
                e.printStackTrace();
                return false;
            } finally {
                if (writer != null)
                    writer.close();
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "writer closing error: ", e);
            e.printStackTrace();
            return true;
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

    public void addDebtor(Debtor c) {
        if(mDebtors.contains(c) == false) {
            mDebtors.add(c);
            sortDebtors();
        }
    }

    public void deleteDebtor(Debtor c) {mDebtors.remove(c); }

    public ArrayList<Debtor> getDebtors() {
        return mDebtors;
    }


    public void sortDebtors () {
        Collections.sort(mDebtors, new DebtorComparator());
    }

    public class DebtorComparator implements Comparator<Debtor> {
        @Override
        public int compare(Debtor o1, Debtor o2) {
            int i = o1.getName().compareTo(o2.getName());
            if (i == 1) {
                return -1;
            } else if (i == -1) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
