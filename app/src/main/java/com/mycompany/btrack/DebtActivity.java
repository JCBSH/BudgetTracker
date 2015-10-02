package com.mycompany.btrack;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mycompany.btrack.models.Debt;
import com.mycompany.btrack.models.Debtor;
import com.mycompany.btrack.models.UserInfo;
import com.mycompany.btrack.utils.DebtComparator;

import java.util.ArrayList;

/**
 * Created by JCBSH on 2/10/2015.
 */
public class DebtActivity extends ListActivity {

    public static final String TAG = DebtActivity.class.getSimpleName();
    private String mName;
    private boolean mDeleteStatus;
    private Debtor mDebtor;
    private ArrayList<Debt> mDebts;
    private ArrayList<Debt> mDeleteDebtsList;
    private ArrayList<Integer> mDeleteListPosition;
    private ImageButton mAddDeleteButton;
    private ImageButton mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt);
        mName =  (String) getIntent().getStringExtra(DebtorFragment.EXTRA_DEBTOR_NAME);
        setTitle("Debts with " + mName);
        mDebtor = UserInfo.get(getApplicationContext()).getDebtor(mName);

        mDeleteStatus = false;
        mDeleteDebtsList = new ArrayList<Debt>();
        mDeleteListPosition = new ArrayList<Integer>();
                mDebts = mDebtor.getDebts();
        DebtAdapter adapter =  new DebtAdapter(DebtActivity.this, mDebts);
        setListAdapter(adapter);

        mAddDeleteButton = (ImageButton) findViewById(R.id.debt_AddDeleteButton);
        mAddDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteStatus == false) {
                    Debt d = new Debt();
                    mDebtor.addDebt(d);
                    UserInfo.get(getApplicationContext()).saveUserInfo();
                    sortAndNotify(((DebtAdapter) getListAdapter()));
                    //startEditTransaction(t);
                } else {
                    for (Integer i : mDeleteListPosition) {
                        CheckedTextView ch = (CheckedTextView) getListView().getChildAt(i).findViewById(R.id.delete_check);
                        ch.setChecked(false);
                    }
                    for (Debt d : mDeleteDebtsList) {
                        mDebtor.deleteDebt(d);
                        mDebts.remove(d);
                    }

                    mDeleteDebtsList.clear();
                    mDeleteListPosition.clear();

                    UserInfo.get(getApplicationContext()).saveUserInfo();
                    sortAndNotify(((DebtAdapter) getListAdapter()));
                    //mDeleteStatus = false;
                }
            }
        });

        mCancelButton = (ImageButton) findViewById(R.id.debt_cancelDeleteButton);
        mCancelButton.setVisibility(View.INVISIBLE);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteStatus = false;
                clearDeleteList();
                adjustButtonDependencyForCancelDelete();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Debt t = ((DebtAdapter)getListAdapter()).getItem(position);
        if (mDeleteStatus == true) {
            CheckedTextView ch = (CheckedTextView) v.findViewById(R.id.delete_check);
            if (ch.isChecked() == true) {
                ch.setChecked(false);
                mDeleteDebtsList.remove(t);
                mDeleteListPosition.remove(position);
            } else {
                ch.setChecked(true);
                mDeleteDebtsList.add(t);
                mDeleteListPosition.add(position);
            }
        }
//        } else {
//            startEditTransaction(t);
//
//        }



    }

    private void sortAndNotify(DebtAdapter listAdapter) {
        listAdapter.sort(new DebtComparator());
        listAdapter.notifyDataSetChanged();
    }
    private void clearDeleteList() {
        for (Integer i : mDeleteListPosition) {
            CheckedTextView ch = (CheckedTextView) getListView().getChildAt(i).findViewById(R.id.delete_check);
            ch.setChecked(false);
        }
        mDeleteDebtsList.clear();
        mDeleteListPosition.clear();
    }
//    private void startEditTransaction(Debt d) {
//        Intent i = new Intent(getActivity(), EditTransactionActivity.class);
//        i.putExtra(EditTransactionActivity.EXTRA_TRANSACTION_ID, t.getId());
//        startActivityForResult(i, REQUEST_SUCCESSFUL_EDIT);
//        //mCallbacks.onTransactionSelected(t);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debt, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_logout:
                App app = (App) getApplicationContext();
                app.getFirebase().unauth();
                Intent intent = new Intent(DebtActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                mDeleteStatus = true;
                adjustButtonDependencyMenuDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void adjustButtonDependencyMenuDelete() {
        mAddDeleteButton.setImageResource(R.drawable.bin);
        mCancelButton.setVisibility(View.VISIBLE);
        mAddDeleteButton.setVisibility(View.VISIBLE);
        ((DebtAdapter)getListAdapter()).notifyDataSetChanged();
    }
//    private void adjustButtonDependencyForCancelFilter() {
//        mAddDeleteButton.setVisibility(View.VISIBLE);
//        mCancelFilterButton.setVisibility(View.INVISIBLE);
//    }
    private void adjustButtonDependencyForCancelDelete() {
        mAddDeleteButton.setImageResource(R.drawable.add);
        mCancelButton.setVisibility(View.INVISIBLE);
//        if (mFilterStatus == true) {
//            mAddDeleteButton.setVisibility(View.INVISIBLE);
//        } else {
//            mAddDeleteButton.setVisibility(View.VISIBLE);
//        }
        ((DebtAdapter) getListAdapter()).notifyDataSetChanged();
    }
//    private void adjustButtonDependencyForApplyFilter() {
//        if (mDeleteStatus == false) {
//            mAddDeleteButton.setVisibility(View.INVISIBLE);
//        } else {
//            mAddDeleteButton.setVisibility(View.VISIBLE);
//            clearDeleteList();
//        }
//    }

    private class DebtAdapter extends ArrayAdapter<Debt> {
        public DebtAdapter(Context context, ArrayList<Debt> debts) {
            super(context, 0, debts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(R.layout.debt_list_item, null);
            }
            Debt c = getItem(position);

            TextView amountTextView =
                    (TextView)convertView.findViewById(R.id.debt_list_item_amount_TextView);
            amountTextView.setText(c.getFormattedAmount());

            TextView dateTextView =
                    (TextView)convertView.findViewById(R.id.debt_list_item_date_TextView);
            dateTextView.setText(c.getFormattedDate2());

            TextView descriptionTextView =
                    (TextView)convertView.findViewById(R.id.debt_list_item_description_TextView);
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
