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
import com.mycompany.btrack.utils.DebtorComparator;
import com.mycompany.btrack.utils.TransactionComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by JCBSH on 27/09/2015.
 */
public class UserInfo {

    private ArrayList<Transaction> mTransactions;
    private ArrayList<Debtor> mDebtors;
    private static UserInfo sUserInfo;
    private Context mAppContext;

    private static final String TAG = UserInfo.class.getSimpleName();

    private SpendingLimit mSpendingLimit;

    private UserInfo(Context appContext) {
        mAppContext = appContext;

        mTransactions = new ArrayList<Transaction>();
        mDebtors = new ArrayList<Debtor>();
        mSpendingLimit = new SpendingLimit(0.00);

        App app = (App) mAppContext.getApplicationContext();

        Firebase userRef = app.getFirebase().child("users").child(app.getUser().getUid()).child("transactions");
        //userRef.da
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

        Firebase limitRef = app.getFirebase().child("users").child(app.getUser().getUid()).child("spending_limit");

        limitRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SpendingLimitDB limit = dataSnapshot.getValue(SpendingLimitDB.class);
                    mSpendingLimit = new SpendingLimit(limit.getAmount());
                    TransactionFragment.mSpendingLimitButton.setText("Spending Limit: $" + limit.getAmount());
//                    TransactionFragment.refresh();
                    //Log.e(TAG, "limit ----------------> " + mSpendingLimit.getAmount());
                } else {

                    Log.e(TAG, "LIMIT does not exist########");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.i(TAG + "onCancelled()#########", firebaseError.getMessage());
            }
        });


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

    private void saveSpendingLimitToDB(Firebase userRef) {
        Firebase limitRef = userRef.child("spending_limit");
        SpendingLimit spendingLimit = new SpendingLimit(mSpendingLimit.getAmount());
        limitRef.setValue(spendingLimit);
        Log.d(TAG, "SAVE spending limit TO DB");
    }

    public boolean saveUserInfo() {
        App app = (App) mAppContext.getApplicationContext();
        Firebase userRef = app.getFirebase().child("users").child(app.getUser().getUid());
        saveTransactionsToDB(userRef);
        saveDebtorsToDB(userRef);
        saveSpendingLimitToDB(userRef);

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


    public SpendingLimit getSpendingLimit() {

        return mSpendingLimit;

    }


    public void setSpendingLimit(double limit) {
            this.mSpendingLimit.setAmount(limit);
    }
}
