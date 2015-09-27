package com.mycompany.btrack;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mycompany.btrack.models.Transaction;
import com.mycompany.btrack.models.UserInfo;

import java.util.ArrayList;

public class TransactionFragment extends ListFragment {

    public static final String TAG = TransactionFragment.class.getSimpleName();

    private ArrayList<Transaction> mTransactions;

    public TransactionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
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
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        mTransactions = UserInfo.get(getActivity()).getTransactions();
        Transaction t = new Transaction();
        t.setAmount(666.77);
        t.setDescription("i brought a keep");
        t.setRecipient("dealership");

        Transaction s = new Transaction();
        s.setAmount(1026.3);
        s.setDescription("i brought a cow");

        Transaction u = new Transaction();
        u.setAmount(1026.3);

        mTransactions.add(t);
        mTransactions.add(s);
        mTransactions.add(u);
        TransactionAdapter adapter = new TransactionAdapter(mTransactions);
        setListAdapter(adapter);

        Log.d(TAG, "onCreate()");
    }


    @Override
    public void onResume() {
        super.onResume();
        ((TransactionAdapter)getListAdapter()).notifyDataSetChanged();
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
            dateTextView.setText(c.getFormattedDate());

            TextView descriptionTextView =
                    (TextView)convertView.findViewById(R.id.transaction_list_item_description_TextView);
            descriptionTextView.setText(c.getDescription());
            return convertView;
        }
    }

}
