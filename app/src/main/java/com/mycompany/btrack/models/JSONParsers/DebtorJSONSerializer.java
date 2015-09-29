package com.mycompany.btrack.models.JSONParsers;

import android.util.Log;

import com.mycompany.btrack.models.Debtor;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by JCBSH on 30/09/2015.
 */
public class DebtorJSONSerializer {
    public static final String TAG = DebtorJSONSerializer.class.getSimpleName();

    public JSONArray createJSONDebtors(ArrayList<Debtor> debtors)
            throws JSONException, IOException {
        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Debtor c : debtors) {
            array.put(c.toJSON());
        }
        return array;
    }

    public ArrayList<Debtor> loadDebtors(JSONArray array) throws IOException, JSONException {
        ArrayList<Debtor> debtors = new ArrayList<Debtor>();
        Log.d(TAG, "loading");

        // Build the array of Transactions from JSONObjects
        for (int i = 0; i < array.length(); i++) {
            debtors.add(new Debtor(array.getJSONObject(i)));
        }
        return debtors;
    }
}
