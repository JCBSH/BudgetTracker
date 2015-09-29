package com.mycompany.btrack.models;

import android.content.Context;
import android.util.Log;

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
    private static UserInfo sUserInfo;
    private Context mAppContext;

    public static final String JSON_TRANSACTIONS = "transactions";

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
        mTransactionSerializer = new TransactionJSONSerializer();
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
                Log.d(TAG, "successfully load transaction");
                sortTransactions();

            } catch (Exception e) {
                mTransactions = new ArrayList<Transaction>();
                Log.e(TAG, "Error loading crimes: ", e);
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
            Writer writer = null;
            try {
                JSONArray transactionsJsonArray = mTransactionSerializer.createJSONTransactions(mTransactions);
                JSONObject userInfoJsonObject = new JSONObject();
                userInfoJsonObject.put(JSON_TRANSACTIONS, transactionsJsonArray);
                OutputStream out = mAppContext
                        .openFileOutput(FILENAME, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(userInfoJsonObject.toString());
                int d = Log.d(TAG, "Transactions saved to file");
            } catch (Exception e) {
                Log.e(TAG, "Error saving Transactions: ", e);
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
    public boolean saveUserInfo() {
        return true;
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
