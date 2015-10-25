package com.mycompany.btrack;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.mycompany.btrack.savedStates.DebtActivityState;
import com.mycompany.btrack.utils.DebtComparator;
import com.mycompany.btrack.utils.InternetUtil;

import java.util.ArrayList;

/**
 * Created by JCBSH on 2/10/2015.
 */
public class DebtActivity extends ListActivity {

    public static final String TAG = DebtActivity.class.getSimpleName();

    private static final int REQUEST_SUCCESSFUL_EDIT = 1;

    private String mName;
    private boolean mDeleteStatus;
    private Debtor mDebtor;
    private ArrayList<Debt> mDebts;
    private ArrayList<Debt> mDeleteDebtsList;
    private ArrayList<Integer> mDeleteListPosition;
    private ImageButton mAddDeleteButton;
    private ImageButton mCancelButton;
    private TextView mBalanceTextView;
    private TextView mStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt);
        mName =  (String) getIntent().getStringExtra(DebtorFragment.EXTRA_DEBTOR_NAME);


        //recovering name for when back button is pressed for EditDebtActivity
        if (mName == null) {
            mName = DebtActivityState.get(getApplicationContext()).getDebtorName();
        }
        //saving name for when back button is pressed for EditDebtActivity
        DebtActivityState.get(getApplicationContext()).setDebtorName(mName);


        Log.d(TAG, "name " + mName);
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
                if (InternetUtil.isNetworkConnected(DebtActivity.this)) {
                    if (mDeleteStatus == false) {
                        Debt d = new Debt();
                        mDebtor.addDebt(d);
                        UserInfo.get(getApplicationContext()).saveUserInfo();
                        sortAndNotify(((DebtAdapter) getListAdapter()));
                        startEditDebt(d);
                    } else {

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
                } else {
                    InternetUtil.alertUserNetwork(DebtActivity.this);
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

        mBalanceTextView = (TextView) findViewById(R.id.debt_balance_TextView);
        mStatusTextView = (TextView) findViewById(R.id.debt_status_TextView);

        mBalanceTextView.setText(mDebtor.getFormatBalance());

        double balance = mDebtor.getBalance();
        String status = "";
        if (balance > 0) {
            status = "LENDING";
            mStatusTextView.setTextColor(Color.parseColor(getString(R.string.positive_green)));
        } else if (balance < 0) {
            status = "OWING";
            mStatusTextView.setTextColor(Color.parseColor(getString(R.string.negative_red)));
        }
        mStatusTextView.setText(status);

        Log.d(TAG, "onCreate()");
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
        } else {
            startEditDebt(t);

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_SUCCESSFUL_EDIT:
                Log.d(TAG, "transaction edit successful");
                sortAndNotify(((DebtAdapter) getListAdapter()));
                break;
        }
    }


    private void sortAndNotify(DebtAdapter listAdapter) {
        mBalanceTextView.setText(mDebtor.getFormatBalance());

        double balance = mDebtor.getBalance();
        String status = "";
        if (balance > 0) {
            status = "LENDING";
            mStatusTextView.setTextColor(Color.parseColor(getString(R.string.positive_green)));
        } else if (balance < 0) {
            status = "OWING";
            mStatusTextView.setTextColor(Color.parseColor(getString(R.string.negative_red)));
        }
        mStatusTextView.setText(status);
        listAdapter.sort(new DebtComparator());
        listAdapter.notifyDataSetChanged();
    }
    private void clearDeleteList() {
        mDeleteDebtsList.clear();
        mDeleteListPosition.clear();
    }
    private void startEditDebt(Debt d) {
        Intent i = new Intent(this, EditDebtActivity.class);
        i.putExtra(EditDebtActivity.EXTRA_DEBT_ID, d.getId());
        i.putExtra(EditDebtActivity.EXTRA_DEBTOR_NAME, mDebtor.getName());
        startActivityForResult(i, REQUEST_SUCCESSFUL_EDIT);
        //mCallbacks.onTransactionSelected(t);
    }


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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }


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
