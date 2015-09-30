package com.mycompany.btrack;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mycompany.btrack.models.Transaction;
import com.mycompany.btrack.models.UserInfo;

import java.util.ArrayList;

public class TransactionFragment extends ListFragment {

    public static final String TAG = TransactionFragment.class.getSimpleName();

    private ArrayList<Transaction> mTransactions;
    private ArrayList<Transaction> mDeleteTransactionsList;
    private ArrayList<Integer> mDeleteListPosition;
    private ImageButton mAddDeleteButton;
    private Callbacks mCallbacks;
    private boolean mDeleteStatus;
    private ImageButton mCancelButton;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onTransactionSelected(Transaction transaction);
    }


    public TransactionFragment() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            //getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mDeleteStatus = true;
            mAddDeleteButton.setImageResource(R.drawable.bin);
            mCancelButton.setVisibility(View.VISIBLE);
            ((TransactionAdapter)getListAdapter()).notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_transaction,
                container, false);
//
//
        //ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        
        mAddDeleteButton = (ImageButton) rootView.findViewById(R.id.transaction_AddDeleteButton);
        mAddDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteStatus == false) {
                    Transaction t = new Transaction();
                    UserInfo.get(getActivity().getApplicationContext()).addTransaction(t);
                    ((TransactionAdapter) getListAdapter()).notifyDataSetChanged();
                    mCallbacks.onTransactionSelected(t);
                } else {
                    for (Integer i : mDeleteListPosition) {
                        CheckedTextView ch = (CheckedTextView) getListView().getChildAt(i).findViewById(R.id.delete_check);
                        ch.setChecked(false);
                    }
                    for (Transaction t : mDeleteTransactionsList) {
                        UserInfo.get(getActivity().getApplicationContext()).deleteTransaction(t);
                        mTransactions.remove(t);
                    }

                    mDeleteTransactionsList.clear();
                    mDeleteListPosition.clear();

                    UserInfo.get(getActivity().getApplicationContext()).saveTransactions();
                    UserInfo.get(getActivity().getApplicationContext()).saveUserInfo();
                    ((TransactionAdapter) getListAdapter()).notifyDataSetChanged();
                    //mDeleteStatus = false;
                }
            }
        });
        
        mCancelButton = (ImageButton) rootView.findViewById(R.id.transaction_cancelDeleteButton);
        mCancelButton.setVisibility(View.INVISIBLE);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer i : mDeleteListPosition) {
                    CheckedTextView ch = (CheckedTextView) getListView().getChildAt(i).findViewById(R.id.delete_check);
                    ch.setChecked(false);
                }
                mDeleteTransactionsList.clear();
                mDeleteListPosition.clear();
                mAddDeleteButton.setImageResource(R.drawable.add);
                mDeleteStatus = false;
                mCancelButton.setVisibility(View.INVISIBLE);
                ((TransactionAdapter) getListAdapter()).notifyDataSetChanged();
            }
        });


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        //mDeleteStatus = false;
        mDeleteStatus = false;

        mTransactions = UserInfo.get(getActivity().getApplicationContext()).getTransactions();
        mDeleteTransactionsList = new ArrayList<Transaction>();
        mDeleteListPosition =  new ArrayList<Integer>();
        TransactionAdapter adapter = new TransactionAdapter(mTransactions);
        setListAdapter(adapter);

        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_transaction_menu, menu);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
        Log.d(TAG, "onAttach()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TransactionAdapter)getListAdapter()).notifyDataSetChanged();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        Log.d(TAG, "onDetach()");
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Transaction t = ((TransactionAdapter)getListAdapter()).getItem(position);
        if (mDeleteStatus == true) {
            CheckedTextView ch = (CheckedTextView) v.findViewById(R.id.delete_check);
            if (ch.isChecked() == true) {
                ch.setChecked(false);
                mDeleteTransactionsList.remove(ch);
                mDeleteListPosition.remove(position);
            } else {
                ch.setChecked(true);
                mDeleteTransactionsList.add(t);
                mDeleteListPosition.add(position);
            }
        } else {

            mCallbacks.onTransactionSelected(t);
        }



    }


    private class TransactionAdapter extends ArrayAdapter<Transaction> {
        public TransactionAdapter(ArrayList<Transaction> transactions) {
            super(getActivity(), 0, transactions);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.transaction_list_item, null);
            }
            Transaction c = getItem(position);
            TextView recipientTextView =
                    (TextView)convertView.findViewById(R.id.transaction_list_item_recipient_TextView);
            recipientTextView.setText(c.getRecipient());

            TextView amountTextView =
                    (TextView)convertView.findViewById(R.id.transaction_list_item_amount_TextView);
            amountTextView.setText(c.getFormattedAmount());

            TextView dateTextView =
                    (TextView)convertView.findViewById(R.id.transaction_list_item_date_TextView);
            dateTextView.setText(c.getFormattedDate2());

            TextView descriptionTextView =
                    (TextView)convertView.findViewById(R.id.transaction_list_item_description_TextView);
            descriptionTextView.setText(c.getDescription());

            CheckedTextView ch = (CheckedTextView) convertView.findViewById(R.id.delete_check);
            if (mDeleteStatus == true) {
                ch.setVisibility(View.VISIBLE);
            } else {
                //ch.setChecked(false);
                ch.setVisibility(View.INVISIBLE);
            }


            return convertView;
        }
    }

}
