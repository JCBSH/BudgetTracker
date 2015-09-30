package com.mycompany.btrack.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JCBSH on 30/09/2015.
 */
public class Debtor {
    private static final String TAG = Debtor.class.getSimpleName();

    private static final String JSON_NAME = "name";
    private static final String DEFAULT_NAME = "person";
    private String mName;
    private static int count = 0;

    public Debtor() {
        mName = getNextDefaultName();
        count++;
    }

    public Debtor(JSONObject json) throws JSONException {
        if (json.has(JSON_NAME)) {
            mName = json.getString(JSON_NAME);
        }
    }

    @Override
    public String toString() {
        return mName;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_NAME, mName);
        return json;
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
}
