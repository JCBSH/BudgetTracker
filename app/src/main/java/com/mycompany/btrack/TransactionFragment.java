package com.mycompany.btrack;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mycompany.btrack.models.Transaction;
import com.mycompany.btrack.models.UserInfo;

import java.util.ArrayList;

public class TransactionFragment extends ListFragment {

    public static final String TAG = TransactionFragment.class.getSimpleName();

    private ArrayList<Transaction> mTransactions;
    private ImageButton mAddButton;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onTransactionSelected(Transaction transaction);
    }


    public TransactionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_transaction,
                container, false);
//
//
//        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//            // Use floating context menus on Froyo and Gingerbread registerForContextMenu(listView);
//            registerForContextMenu(listView);
//        } else {
//            // Use contextual action bar on Honeycomb and higher
//            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        }

        mAddButton = (ImageButton) rootView.findViewById(R.id.transaction_AddButton);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transaction t = new Transaction();
                UserInfo.get(getActivity().getApplicationContext()).addTransaction(t);
                ((TransactionAdapter)getListAdapter()).notifyDataSetChanged();
                mCallbacks.onTransactionSelected(t);
            }
        });



        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        mTransactions = UserInfo.get(getActivity().getApplicationContext()).getTransactions();
//        Transaction t = new Transaction();
//        t.setAmount(666.77);
//        t.setDescription("i brought a keep");
//        t.setRecipient("dealership");
//
//        Transaction s = new Transaction();
//        s.setAmount(1026.3);
//        s.setDescription("i brought a cow");
//
//        Transaction u = new Transaction();
//        u.setAmount(1026.3);
//
//        mTransactions.add(t);
//        mTransactions.add(s);
//        mTransactions.add(u);
        TransactionAdapter adapter = new TransactionAdapter(mTransactions);
        setListAdapter(adapter);

        Log.d(TAG, "onCreate()");
    }


    @Override
    public void onResume() {
        super.onResume();
        ((TransactionAdapter)getListAdapter()).notifyDataSetChanged();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
        Log.d(TAG, "onAttach()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        Log.d(TAG, "onDetach()");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Get the Crime from the adapter
        Transaction t = ((TransactionAdapter)getListAdapter()).getItem(position);
        // Start CrimePagerActivity
        mCallbacks.onTransactionSelected(t);
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
            return convertView;
        }
    }

}
