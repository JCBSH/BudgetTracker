package com.mycompany.btrack;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.btrack.models.SpendingLimit;
import com.mycompany.btrack.models.Transaction;
import com.mycompany.btrack.models.UserInfo;
import com.mycompany.btrack.savedStates.HomeActivityTabState;
import com.mycompany.btrack.savedStates.TransactionFragmentState;
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
    public static final String  FILTER_CATEGORY_BUNDLE_KEY = "category";
    public static final String  FILTER_PRIORITY_BUNDLE_KEY = "priority";
    private static final int REQUEST_FILTER_INFO = 0;
    private static final int REQUEST_SUCCESSFUL_EDIT = 1;
    private static final int REQUEST_NEW_SPENDING = 2;
    private static final String EDIT_FILTER_INFO = "edit filter info";


    private ArrayList<Transaction> mTransactions;
    private ArrayList<Transaction> mDeleteTransactionsList;
    private ArrayList<Integer> mDeleteListPosition;
    private ImageButton mAddDeleteButton;
    private boolean mDeleteStatus;
    private ImageButton mCancelButton;
    private ImageButton mFilterButton;
    private ImageButton mCancelFilterButton;
    private ImageButton mSummaryButton;

    private TransactionFragmentState saveState;

    public static TextView mTransactionSummaryTextView;
    public static Button mSpendingLimitButton;

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

        mTransactionSummaryTextView = (TextView) rootView.findViewById(R.id.all_transaction_summary_text);
        mTransactionSummaryTextView.setText(Transaction.totalAmountSpanForTextView(
                (UserInfo.get(getActivity().getApplicationContext())).getTransactions(), getActivity()));


        mSpendingLimitButton = (Button) rootView.findViewById(R.id.set_up_limit_button);

        updateSpendingLimit();

        mSpendingLimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SetUpLimitActivity.class);
                startActivityForResult(i, REQUEST_NEW_SPENDING);

            }
        });

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

                    for (Transaction t : mDeleteTransactionsList) {
                        UserInfo.get(getActivity().getApplicationContext()).deleteTransaction(t);
                        mTransactions.remove(t);
                    }

                    clearDeleteList();

                    UserInfo.get(getActivity().getApplicationContext()).saveUserInfo();

                    sortAndNotify(((TransactionAdapter) getListAdapter()));
                    updateSpendingLimit();
                    mTransactionSummaryTextView.setText(Transaction.totalAmountSpanForTextView(
                            (UserInfo.get(getActivity().getApplicationContext())).getTransactions(), getActivity()));
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
                if (saveState.isFilterStatus() == true) {

                    bundle.putSerializable(FILTER_FROM_DATE_BUNDLE_KEY, (Serializable) saveState.getFilterFromDate());
                    bundle.putSerializable(FILTER_TO_DATE_BUNDLE_KEY, (Serializable) saveState.getFilterToDate());
                    bundle.putDouble(FILTER_AMOUNT_FROM_BUNDLE_KEY,  saveState.getFilterAmountFrom());
                    bundle.putDouble(FILTER_AMOUNT_TO_BUNDLE_KEY,  saveState.getFilterAmountTo());
                    bundle.putString(FILTER_RECIPIENT_BUNDLE_KEY,  saveState.getFilterRecipient());
                    bundle.putString(FILTER_DESCRIPTION_BUNDLE_KEY,  saveState.getFilterDescription());
                    bundle.putString(FILTER_CATEGORY_BUNDLE_KEY,  saveState.getFilterCategory());
                    bundle.putString(FILTER_PRIORITY_BUNDLE_KEY,  saveState.getFilterPriority());

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
                saveState.setFilterStatus(false);
                adjustButtonDependencyForFilterStatus();
                mTransactions = getTransactionList();
                setAdapterFromTransactions();

                HomeActivityTabState homeActivityTabState = HomeActivityTabState.get(getActivity().getApplicationContext());
                String newTitle = getActivity().getString(R.string.TransactionFragment_all_transaction_title);
                homeActivityTabState.setTransactionFragmentTitle(newTitle);
                getActivity().setTitle(newTitle);

            }
        });

        mSummaryButton = (ImageButton) rootView.findViewById(R.id.transaction_SummaryButton);
        mSummaryButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 FragmentManager fm = getActivity().getSupportFragmentManager();
                 SummarySelectionFragment fragment = new SummarySelectionFragment();
                 fragment.setTargetFragment(TransactionFragment.this, REQUEST_FILTER_INFO);
                 fragment.show(fm, EDIT_FILTER_INFO);
             }
        });

        adjustButtonDependencyForFilterStatus();
        transactionFragment = this;
        return rootView;
    }
    static TransactionFragment transactionFragment;
    public static void refresh() {
        ((TransactionAdapter) transactionFragment.getListAdapter()).notifyDataSetChanged();

//        mSpendingLimitButton = (Button) rootView.findViewById(R.id.set_up_limit_button);
//
//        SpendingLimit limit = UserInfo.get(getActivity().getApplicationContext()).getSpendingLimit();
//        double amount = limit.getAmount();
//        mSpendingLimitButton.setText("Spending Limit: $" + amount);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_FILTER_INFO:
                saveState.setFilterStatus(true);
                adjustButtonDependencyForFilterStatus();
                TransactionFragmentState saveState = TransactionFragmentState.get(getActivity().getApplicationContext());
                saveState.setFilterFromDate((Date) data
                        .getSerializableExtra(FilterTransactionFragment.EXTRA_FROM_DATE));

                saveState.setFilterToDate((Date) data
                        .getSerializableExtra(FilterTransactionFragment.EXTRA_TO_DATE));

                saveState.setFilterAmountFrom(data.getDoubleExtra(FilterTransactionFragment.EXTRA_AMOUNT_FROM, 0));
                saveState.setFilterAmountTo(data.getDoubleExtra(FilterTransactionFragment.EXTRA_AMOUNT_TO, 0));
                saveState.setFilterRecipient(data.getStringExtra(FilterTransactionFragment.EXTRA_RECIPIENT));
                saveState.setFilterDescription(data.getStringExtra(FilterTransactionFragment.EXTRA_DESCRIPTION));
                saveState.setFilterCategory(data.getStringExtra(FilterTransactionFragment.EXTRA_CATEGORY));
                saveState.setFilterPriority(data.getStringExtra(FilterTransactionFragment.EXTRA_PRIORITY));

                Log.d(TAG, String.format("unfiltered size: %d", mTransactions.size()));
                mTransactions = getTransactionList();


                Log.d(TAG, String.format("filtered size: %d", mTransactions.size()));
                setAdapterFromTransactions();


                HomeActivityTabState homeActivityTabState = HomeActivityTabState.get(getActivity().getApplicationContext());
                String newTitle = newTitle = getActivity().getString(R.string.TransactionFragment_filtered_transaction_title);
                homeActivityTabState.setTransactionFragmentTitle(newTitle);
                getActivity().setTitle(newTitle);
                break;
            case REQUEST_SUCCESSFUL_EDIT:
                Log.d(TAG, "transaction edit successful");
                sortAndNotify(((TransactionAdapter) getListAdapter()));
                updateSpendingLimit();
                mTransactionSummaryTextView.setText(Transaction.totalAmountSpanForTextView(
                        (UserInfo.get(getActivity().getApplicationContext())).getTransactions(), getActivity()));
                break;
            case REQUEST_NEW_SPENDING:
                double newLimit = data.getDoubleExtra(SetUpLimitActivity.EXTRA_NEW_LIMIT, 0.00);
                SpendingLimit limit = UserInfo.get(getActivity().getApplicationContext()).getSpendingLimit();
                limit.setLimit(newLimit);
                updateSpendingLimit();
                UserInfo.get(getActivity().getApplicationContext()).saveUserInfo();

        }
    }

    public ArrayList<Transaction> getTransactionList() {
        if (saveState.isFilterStatus() == true) {
            return getFilteredList();
        } else {
            return UserInfo.get(getActivity().getApplicationContext()).getTransactions();
        }
    }

    public ArrayList<Transaction> getFilteredList() {
        return Transaction.filterTransactions(UserInfo.get(getActivity().getApplicationContext()).getTransactions(),
                saveState.getFilterFromDate(), saveState.getFilterToDate(),
                saveState.getFilterAmountFrom(), saveState.getFilterAmountTo(),
                saveState.getFilterRecipient(), saveState.getFilterDescription(),
                saveState.getFilterCategory(), saveState.getFilterPriority());
    }


    private void updateSpendingLimit() {
        SpendingLimit limit = UserInfo.get(getActivity().getApplicationContext()).getSpendingLimit();
        limit.update();
        mSpendingLimitButton.setText(limit.toString());

        if (limit.overStatus()) {
            mSpendingLimitButton.setTextColor(Color.parseColor(getString(R.string.negative_red)));
        } else {
            mSpendingLimitButton.setTextColor(Color.parseColor(getString(R.string.positive_green)));
        }
        if (limit.overStatus()) {
            Toast.makeText(this.getActivity(), "Spending limit has been reached!!!", Toast.LENGTH_SHORT).show();
        }
    }


    private void sortAndNotify(TransactionAdapter listAdapter) {
        listAdapter.sort(new TransactionComparator());
        listAdapter.notifyDataSetChanged();
        //updateSpendingLimit();
    }
    private void clearDeleteList() {
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

    private void adjustButtonDependencyForCancelDelete() {
        mAddDeleteButton.setImageResource(R.drawable.add);
        mCancelButton.setVisibility(View.INVISIBLE);
        if (saveState.isFilterStatus() == true) {
            mAddDeleteButton.setVisibility(View.INVISIBLE);
        } else {
            mAddDeleteButton.setVisibility(View.VISIBLE);
        }
        ((TransactionAdapter) getListAdapter()).notifyDataSetChanged();
    }
    private void adjustButtonDependencyForFilterStatus() {
        if (saveState.isFilterStatus() == true) {
            if (mDeleteStatus == false) {
                mAddDeleteButton.setVisibility(View.INVISIBLE);
            } else {
                mAddDeleteButton.setVisibility(View.VISIBLE);
                clearDeleteList();
            }
            mCancelFilterButton.setVisibility(View.VISIBLE);
        } else {
            clearDeleteList();
            mAddDeleteButton.setVisibility(View.VISIBLE);
            mCancelFilterButton.setVisibility(View.INVISIBLE);
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
        saveState = TransactionFragmentState.get(getActivity().getApplicationContext());
        //mDeleteStatus = false;
        mDeleteStatus = false;

        mDeleteTransactionsList = new ArrayList<Transaction>();
        mDeleteListPosition =  new ArrayList<Integer>();

        mTransactions = getTransactionList();
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
        Log.d(TAG, "onDetach()");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "selected: " + position + "  : " +((TextView) v.findViewById(R.id.transaction_list_item_amount_TextView)).getText());
        Transaction t = ((TransactionAdapter)getListAdapter()).getItem(position);
        if (mDeleteStatus == true) {
            CheckedTextView ch = (CheckedTextView) v.findViewById(R.id.delete_check);

            if (ch.isChecked() == true) {
                ch.setChecked(false);
                mDeleteTransactionsList.remove(t);
                mDeleteListPosition.remove(new Integer(position));
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
//            Log.d(TAG, "total positions: " + UserInfo.get(getActivity().getApplicationContext()).getTransactions().size());
//            Log.d(TAG, "display position: " + position);
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

            ImageView categoryImageView =
                    (ImageView)convertView.findViewById(R.id.transaction_list_item_category_icon_ImageView);
            categoryImageView.setImageResource
                    (getResources().getIdentifier(c.getCategoryIconResource(), null, getActivity().getPackageName()));

            ImageView priorityImageView =
                    (ImageView)convertView.findViewById(R.id.transaction_list_item_priority_icon_ImageView);
            priorityImageView.setImageResource
                    (getResources().getIdentifier(c.getPriorityIconResource(), null, getActivity().getPackageName()));

            CheckedTextView ch = (CheckedTextView) convertView.findViewById(R.id.delete_check);
            if (mDeleteStatus == true) {
                ch.setVisibility(View.VISIBLE);
                if (mDeleteListPosition.contains(position)) {
                    ch.setChecked(true);
                } else {
                    ch.setChecked(false);
                }
            } else {
                //ch.setChecked(false);
                ch.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }
    }

}
