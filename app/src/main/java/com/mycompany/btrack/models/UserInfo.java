package com.mycompany.btrack.models;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mycompany.btrack.App;
import com.mycompany.btrack.DebtorFragment;
import com.mycompany.btrack.TransactionFragment;
import com.mycompany.btrack.models.JSONParsers.DebtorJSONSerializer;
import com.mycompany.btrack.models.JSONParsers.TransactionJSONSerializer;
import com.mycompany.btrack.utils.DebtorComparator;
import com.mycompany.btrack.utils.TransactionComparator;

import org.json.JSONArray;
import org.json.JSONException;
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
        mJsonObject = new JSONObject();
        App app = (App) mAppContext.getApplicationContext();
        Firebase userRef = app.getFirebase().child("users").child(app.getUser().getUid()).child("transactions");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTransactions.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot transaction : dataSnapshot.getChildren()) {
                        TransactionDB t = transaction.getValue(TransactionDB.class);
                        mTransactions.add(new Transaction(t));
                    }
                    sortTransactions();
                    TransactionFragment.refresh();
                } else {
                    Log.i(TAG, "transactions does not exist########");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.i(TAG + "onCancelled()#########", firebaseError.getMessage());
            }
        });
        Firebase debtorRef = app.getFirebase().child("users").child(app.getUser().getUid()).child("debtors");
        debtorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDebtors.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot debtor : dataSnapshot.getChildren()) {
                        DebtorDB d = debtor.getValue(DebtorDB.class);
                        mDebtors.add(new Debtor(d));
                    }
                    sortDebtors();
                    DebtorFragment.refresh();
                } else {
                    Log.i(TAG, "debtors does not exist########");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.i(TAG + "onCancelled()#########", firebaseError.getMessage());
            }
        });
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
                Log.i(TAG + "json string**********", jsonString.toString());

                mJsonObject = new JSONObject(jsonString.toString());
                Log.d(TAG, mJsonObject.toString());


                try {
                    JSONArray transactionsJSONArray = mJsonObject.getJSONArray(UserInfo.JSON_TRANSACTIONS);
                    mTransactions = mTransactionSerializer.loadTransactions(transactionsJSONArray);
                } catch (JSONException e) {
                    mTransactions = new ArrayList<Transaction>();
                    Log.d(TAG, "Error loading Transactions, possibly that it doesn't exist");
                }

                try {
                    JSONArray debtorsJSONArray = mJsonObject.getJSONArray(UserInfo.JSON_DEBTORS);
                    mDebtors = mDebtorSerializer.loadDebtors(debtorsJSONArray);
                } catch (JSONException e) {
                    mDebtors = new ArrayList<Debtor>();
                    Log.d(TAG, "Error loading Debtors, possibly that it doesn't exist");
                }
                try {
                    int debtorsCount = mJsonObject.getInt(JSON_DEBTOR_COUNT);
                    Debtor.setCount(debtorsCount);
                } catch (JSONException e) {
                    Debtor.setCount(0);
                    Log.d(TAG, "Error loading Debtor count, possibly that it doesn't exist");
                }

                Log.d(TAG, "successfully load transaction");
                sortTransactions();
                sortDebtors();

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

    public void destroyUser() {
        sUserInfo = null;
    }

    private boolean saveTransactions() {
        try {
            JSONArray transactionsJsonArray = mTransactionSerializer.createJSONTransactions(mTransactions);
            mJsonObject.put(JSON_TRANSACTIONS, transactionsJsonArray);
            Log.d(TAG, "Transactions saved to JSONObject");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving Transactions: ", e);
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveDebtors() {
        try {
            JSONArray debtorsJsonArray = mDebtorSerializer.createJSONDebtors(mDebtors);
            mJsonObject.put(JSON_DEBTORS, debtorsJsonArray);

            mJsonObject.put(JSON_DEBTOR_COUNT, Debtor.getCount());

            Log.d(TAG, "Debtors saved to JSONObject");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving Debtors: ", e);
            e.printStackTrace();
            return false;
        }
    }

    private void saveTransactionsToDB(Firebase userRef) {
        Log.d(TAG, "SAVE TRANSACTIONS TO DB ######################################");
        Firebase transactionRef = userRef.child("transactions");
        transactionRef.setValue(null);
        for (Transaction transaction : mTransactions) {
            transactionRef.push().setValue(new TransactionDB(transaction), new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        Log.d(TAG, "DB ERROR " + firebaseError.getMessage());
                    } else {
                        Log.d(TAG, "DB SAVED");
                    }
                }
            });
        }
    }
    private void saveDebtorsToDB(Firebase userRef) {
        Log.d(TAG, "SAVE DEBTORS TO DB !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Firebase debtorsRef = userRef.child("debtors");
        debtorsRef.setValue(null);
        for (Debtor debtor : mDebtors) {
            debtorsRef.push().setValue(new DebtorDB(debtor), new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        Log.d(TAG, "DB ERROR " + firebaseError.getMessage());
                    } else {
                        Log.d(TAG, "DB SAVED");
                    }
                }
            });
        }
    }
    public boolean saveUserInfo() {
        saveDebtors();
        saveTransactions();
        App app = (App) mAppContext.getApplicationContext();
        Firebase userRef = app.getFirebase().child("users").child(app.getUser().getUid());
        saveTransactionsToDB(userRef);
        saveDebtorsToDB(userRef);
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



    //LEGACY code, useless
    public void addDebtor(Debtor c) {
        if(mDebtors.contains(c) == false) {
            mDebtors.add(c);
            sortDebtors();
        }
    }

    public Debtor createAndAddNewDebtor() {
        Debtor debtor =  new Debtor();
        int i = 1;
        debtor.setName(Debtor.getDefaultName() + String.valueOf(i));
        while(mDebtors.contains(debtor) == true) {
            i++;
            debtor.setName(Debtor.getDefaultName() + String.valueOf(i));
        }
        mDebtors.add(debtor);
        return debtor;
    }


    public boolean changeName (Debtor c, String newName) {
        String oldName = c.getName();
        Debtor temp = new Debtor();
        temp.setName(newName);
        Debtor.setCount(Debtor.getCount() - 1);
        Log.d(TAG, oldName);
        Log.d(TAG, newName);
        Log.d(TAG, "attempting to changeName");
        if (mDebtors.contains(temp) == true) {
            Log.d(TAG, "attempt failed");
            return false;
        } else {
            c.setName(newName);
            Log.d(TAG, "attempt success");
            return true;
        }
    }

    public void deleteDebtor(Debtor c) {mDebtors.remove(c); }

    public ArrayList<Debtor> getDebtors() {
        return mDebtors;
    }


    public void sortDebtors () {
        Collections.sort(mDebtors, new DebtorComparator());
    }

    public Debtor getDebtor(String name) {
        for (Debtor debtor:mDebtors) {
            if (debtor.getName().equalsIgnoreCase(name)) return debtor;
        }
        return null;
    }
}
