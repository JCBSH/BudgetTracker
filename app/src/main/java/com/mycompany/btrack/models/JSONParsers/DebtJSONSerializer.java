package com.mycompany.btrack.models.JSONParsers;

import android.util.Log;

import com.mycompany.btrack.models.Debt;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by JCBSH on 2/10/2015.
 */


public class DebtJSONSerializer {
    public static final String TAG = DebtJSONSerializer.class.getSimpleName();

    public JSONArray createJSONDebts(ArrayList<Debt> debts)
            throws JSONException, IOException {
        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Debt c : debts) {
            array.put(c.toJSON());
        }
        return array;
    }

    public ArrayList<Debt> loadDebts(JSONArray array) throws IOException, JSONException {
        ArrayList<Debt> debts = new ArrayList<Debt>();
        Log.d(TAG, "loading");

        // Build the array of debts from JSONObjects
        for (int i = 0; i < array.length(); i++) {
            debts.add(new Debt(array.getJSONObject(i)));
        }
        return debts;
    }
}
