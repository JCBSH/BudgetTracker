package com.mycompany.btrack;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import com.mycompany.btrack.utils.TransactionComparator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class TransactionFragment extends ListFragment {

    public static final String TAG = TransactionFragment.class.getSimpleName();

    public static final String  FILTER_FROM_DATE_BUNDLE_KEY = "from date";
    public static final String  FILTER_TO_DATE_BUNDLE_KEY = "to date";
    public static final String  FILTER_RECIPIENT_BUNDLE_KEY = "recipient";
    public static final String  FILTER_AMOUNT_FROM_BUNDLE_KEY = "amount from";
    public static final String  FILTER_AMOUNT_TO_BUNDLE_KEY = "amount to";
    public static final String  FILTER_DESCRIPTION_BUNDLE_KEY = "description";

    private static final int REQUEST_FILTER_INFO = 0;
    private static final int REQUEST_SUCCESSFUL_EDIT = 1;
    private static final String EDIT_FILTER_INFO = "edit filter info";


    private ArrayList<Transaction> mTransactions;
    private ArrayList<Transaction> mDeleteTransactionsList;
    private ArrayList<Integer> mDeleteListPosition;
    private ImageButton mAddDeleteButton;
    private Callbacks mCallbacks;
    private boolean mDeleteStatus;
    private ImageButton mCancelButton;
    private ImageButton mFilterButton;
    private ImageButton mCancelFilterButton;
    private boolean mFilterStatus;
    private Date mFilterFromDate;
    private Date mFilterToDate;
    private double mFilterAmountFrom;
    private double mFilterAmountTo;
    private String mFilterRecipient;
    private String mFilterDescription;

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
            mDeleteStatus = true;
            adjustButtonDependencyMenuDelete();
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
                    sortAndNotify(((TransactionAdapter) getListAdapter()));
                    startEditTransaction(t);
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
                    sortAndNotify(((TransactionAdapter) getListAdapter()));
                    //mDeleteStatus = false;
                }
            }
        });
        
        mCancelButton = (ImageButton) rootView.findViewById(R.id.transaction_cancelDeleteButton);
        mCancelButton.setVisibility(View.INVISIBLE);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteStatus = false;
                clearDeleteList();
                adjustButtonDependencyForCancelDelete();
            }
        });

        mFilterButton = (ImageButton) rootView.findViewById(R.id.transaction_FilterButton);
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FilterTransactionFragment filterTransactionFragment = new FilterTransactionFragment();
                Bundle bundle = new Bundle();
                if (mFilterStatus == true) {
                    bundle.putSerializable(FILTER_FROM_DATE_BUNDLE_KEY, (Serializable) mFilterFromDate);
                    bundle.putSerializable(FILTER_TO_DATE_BUNDLE_KEY, (Serializable) mFilterToDate);
                    bundle.putDouble(FILTER_AMOUNT_FROM_BUNDLE_KEY,  mFilterAmountFrom);
                    bundle.putDouble(FILTER_AMOUNT_TO_BUNDLE_KEY,  mFilterAmountTo);
                    bundle.putString(FILTER_RECIPIENT_BUNDLE_KEY,  mFilterRecipient);
                    bundle.putString(FILTER_DESCRIPTION_BUNDLE_KEY,  mFilterDescription);

                }
                filterTransactionFragment.setArguments(bundle);
                filterTransactionFragment.setTargetFragment(TransactionFragment.this, REQUEST_FILTER_INFO);
                filterTransactionFragment.show(fm, EDIT_FILTER_INFO);
            }
        });

        mCancelFilterButton = (ImageButton) rootView.findViewById(R.id.transaction_cancelFilterButton);
        mCancelFilterButton.setVisibility(View.INVISIBLE);
        mCancelFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilterStatus = false;
                adjustButtonDependencyForCancelFilter();
                mTransactions = UserInfo.get(getActivity().getApplicationContext()).getTransactions();
                setAdapterFromTransactions();
            }
        });



        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_FILTER_INFO:
                mFilterStatus = true;
                adjustButtonDependencyForApplyFilter();
                mFilterFromDate = (Date) data
                        .getSerializableExtra(FilterTransactionFragment.EXTRA_FROM_DATE);

                mFilterToDate = (Date) data
                        .getSerializableExtra(FilterTransactionFragment.EXTRA_TO_DATE);

                mFilterAmountFrom = data.getDoubleExtra(FilterTransactionFragment.EXTRA_AMOUNT_FROM, 0);
                mFilterAmountTo =  data.getDoubleExtra(FilterTransactionFragment.EXTRA_AMOUNT_TO, 0);
                mFilterRecipient = data.getStringExtra(FilterTransactionFragment.EXTRA_RECIPIENT);
                mFilterDescription = data.getStringExtra(FilterTransactionFragment.EXTRA_DESCRIPTION);
                mCancelFilterButton.setVisibility(View.VISIBLE);
                mTransactions = UserInfo.get(getActivity().getApplicationContext()).getTransactions();
                Log.d(TAG, String.format("unfiltered size: %d", mTransactions.size()));
                mTransactions = Transaction.filterTransactions(mTransactions, mFilterFromDate, mFilterToDate,
                        mFilterAmountFrom, mFilterAmountTo,
                        mFilterRecipient, mFilterDescription);

                Log.d(TAG, String.format("filtered size: %d", mTransactions.size()));
                setAdapterFromTransactions();

                break;
            case REQUEST_SUCCESSFUL_EDIT:
                Log.d(TAG, "transaction edit successful");
                sortAndNotify(((TransactionAdapter) getListAdapter()));
                break;
        }
    }

    private void sortAndNotify(TransactionAdapter listAdapter) {
        listAdapter.sort(new TransactionComparator());
        listAdapter.notifyDataSetChanged();
    }
    private void clearDeleteList() {
        for (Integer i : mDeleteListPosition) {
            CheckedTextView ch = (CheckedTextView) getListView().getChildAt(i).findViewById(R.id.delete_check);
            ch.setChecked(false);
        }
        mDeleteTransactionsList.clear();
        mDeleteListPosition.clear();
    }
    private void startEditTransaction(Transaction t) {
        Intent i = new Intent(getActivity(), EditTransactionActivity.class);
        i.putExtra(EditTransactionActivity.EXTRA_TRANSACTION_ID, t.getId());
        startActivityForResult(i, REQUEST_SUCCESSFUL_EDIT);
        //mCallbacks.onTransactionSelected(t);
    }
    private void adjustButtonDependencyMenuDelete() {
        mAddDeleteButton.setImageResource(R.drawable.bin);
        mCancelButton.setVisibility(View.VISIBLE);
        mAddDeleteButton.setVisibility(View.VISIBLE);
        ((TransactionAdapter)getListAdapter()).notifyDataSetChanged();
    }
    private void adjustButtonDependencyForCancelFilter() {
        mAddDeleteButton.setVisibility(View.VISIBLE);
        mCancelFilterButton.setVisibility(View.INVISIBLE);
    }
    private void adjustButtonDependencyForCancelDelete() {
        mAddDeleteButton.setImageResource(R.drawable.add);
        mCancelButton.setVisibility(View.INVISIBLE);
        if (mFilterStatus == true) {
            mAddDeleteButton.setVisibility(View.INVISIBLE);
        } else {
            mAddDeleteButton.setVisibility(View.VISIBLE);
        }
        ((TransactionAdapter) getListAdapter()).notifyDataSetChanged();
    }
    private void adjustButtonDependencyForApplyFilter() {
        if (mDeleteStatus == false) {
            mAddDeleteButton.setVisibility(View.INVISIBLE);
        } else {
            mAddDeleteButton.setVisibility(View.VISIBLE);
            clearDeleteList();
        }
    }
    private void setAdapterFromTransactions() {
        TransactionAdapter adapter = new TransactionAdapter(mTransactions);
        adapter.sort(new TransactionComparator());
        setListAdapter(adapter);
        //((TransactionAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        //mDeleteStatus = false;
        mDeleteStatus = false;
        mFilterStatus = false;

        mFilterFromDate = new Date();
        mFilterToDate = new Date();
        mFilterAmountFrom = 0;
        mFilterAmountTo = 0;
        mFilterRecipient = "";
        mFilterDescription = "";

        mDeleteTransactionsList = new ArrayList<Transaction>();
        mDeleteListPosition =  new ArrayList<Integer>();
        mTransactions = UserInfo.get(getActivity().getApplicationContext()).getTransactions();
        setAdapterFromTransactions();
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
            startEditTransaction(t);

        }



    }



    private class TransactionAdapter extends ArrayAdapter<Transaction> {
        public TransactionAdapter(ArrayList<Transaction> transactions) {
            super(getActivity(), 0, transactions);
        }


        @Override
        public void sort(Comparator<? super Transaction> comparator) {
            super.sort(comparator);
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
