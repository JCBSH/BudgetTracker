package com.mycompany.btrack.models.JSONParsers;

import android.util.Log;

import com.mycompany.btrack.models.Transaction;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by JCBSH on 28/09/2015.
 */
public class TransactionJSONSerializer {
    public static final String TAG = TransactionJSONSerializer.class.getSimpleName();

    public JSONArray createJSONTransactions(ArrayList<Transaction> transactions)
            throws JSONException, IOException {
        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Transaction c : transactions) {
            array.put(c.toJSON());
        }
        return array;
    }

    public ArrayList<Transaction> loadTransactions(JSONArray array) throws IOException, JSONException {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        Log.d(TAG, "loading");

        // Build the array of Transactions from JSONObjects
        for (int i = 0; i < array.length(); i++) {
            transactions.add(new Transaction(array.getJSONObject(i)));
        }
        return transactions;
    }
}
