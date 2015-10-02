package com.mycompany.btrack.models;

import android.util.Log;

import com.mycompany.btrack.models.JSONParsers.DebtJSONSerializer;
import com.mycompany.btrack.utils.DebtComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by JCBSH on 30/09/2015.
 */
public class Debtor {
    private static final String TAG = Debtor.class.getSimpleName();

    private static final String JSON_NAME = "name";
    private static final String JSON_DEBTS = "debts";
    private static final String DEFAULT_NAME = "person";
    private JSONObject mJsonObject;
    private DebtJSONSerializer mDebtSerializer;
    private String mName;
    private static int count = 0;
    private ArrayList<Debt> mDebts;
    private JSONArray mDebtsJSONArray;

    public Debtor() {
        mName = getNextDefaultName();
        mDebts =  new ArrayList<Debt>();
        mDebtSerializer = new DebtJSONSerializer();
        mJsonObject = new JSONObject();
        mDebtsJSONArray = new JSONArray();
        count++;
    }

    public Debtor(JSONObject json) throws JSONException {
        mDebtSerializer = new DebtJSONSerializer();
        mJsonObject = json;
        mDebtsJSONArray = new JSONArray();
        if (json.has(JSON_NAME)) {
            mName = json.getString(JSON_NAME);
        }

        if (json.has(JSON_DEBTS)) {
            try {
                JSONArray debtorsJSONArray = json.getJSONArray(JSON_DEBTS);
                mDebts = mDebtSerializer.loadDebts(debtorsJSONArray);
            } catch (JSONException e) {
                mDebts = new ArrayList<Debt>();
                Log.d(TAG, "Error loading Debts, possibly that it doesn't exist");
            } catch (IOException e) {
                mDebts = new ArrayList<Debt>();
                Log.d(TAG, "Error loading Debts, possibly that it doesn't exist");
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return mName;
    }

    public JSONObject toJSON() throws JSONException{
        saveDebts();
        mJsonObject.put(JSON_NAME, mName);
        mJsonObject.put(JSON_DEBTS, mDebtsJSONArray);
        Log.d(TAG, "Debtor json returned");
        Log.d(TAG, String.valueOf(mJsonObject));
        return mJsonObject;
    }

    private boolean saveDebts() {
        try {
            mDebtsJSONArray = mDebtSerializer.createJSONDebts(mDebts);
            Log.d(TAG, "Debts saved to JSONObject");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving Debts: ", e);
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalDebtsAmount () {
        Double total = 0.0;
        for (Debt d: mDebts) {
            total += d.getAmount();
        }
        return total;
    }

    public String getFormatTotalDebtsAmount () {
        double total = getTotalDebtsAmount();
        DecimalFormat df = new DecimalFormat("#.00");
        if (total == 0) {
            return "0.00";
        } else {
            String formatted = df.format(total);
            return formatted;
        }
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Debtor.count = count;
    }

    public static String getNextDefaultName() {
        return DEFAULT_NAME + String.valueOf(count);
    }

    public static String getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    public boolean equals(Object o) {
        Debtor o1 = (Debtor) o;
        return mName.equalsIgnoreCase(((Debtor) o).getName());
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<Debt> getDebts() {
        return mDebts;
    }

    public void addDebt(Debt d) {
        mDebts.add(d);
        sortDebts();
    }

    public void sortDebts () {
        Collections.sort(mDebts, new DebtComparator());
    }

    public void deleteDebt(Debt d) {
        mDebts.remove(d);
    }

    public Debt getDebt(UUID uuid) {
        for (Debt debt:mDebts) {
            if (debt.getId().equals(uuid)) return debt;
        }
        return null;
    }
}
