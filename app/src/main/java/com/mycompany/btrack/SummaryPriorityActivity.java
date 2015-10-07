package com.mycompany.btrack;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.btrack.models.Transaction;
import com.mycompany.btrack.models.UserInfo;
import com.mycompany.btrack.utils.MoneyTextWatcher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class SummaryPriorityActivity extends ListActivity implements MonthPickerFragment.Callbacks{

    public static final String TAG = SummaryPriorityActivity.class.getSimpleName();
    private static final String DIALOG_DATE = "date";
    private Date mDate;
    private Button mMonthButton;
    private ArrayList<String> mPriority;
    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private ArrayList<Transaction> mTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_priority);

        mMonthButton = (Button) findViewById(R.id.summary_Category_Activity_month_Button);
        mDate = new Date();
        mCalendar = Calendar.getInstance();
        //initializing
        setupMonthAndYearAndList();

        setDateButtonText();
        mPriority = Transaction.getPriorityChoices();
        PriorityAdapter adapter =  new PriorityAdapter(SummaryPriorityActivity.this, mPriority);
        setListAdapter(adapter);

        mMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                //TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());


                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_DATE);
                if (prev != null) {
                    Log.d(TAG, "MonthPickerFragment removed");
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                MonthPickerFragment dialog = MonthPickerFragment.newInstance(mDate);
                Log.d(TAG, "MonthPickerFragment added");
                dialog.show(ft, DIALOG_DATE);
            }
        });

        Log.d(TAG, "onCreate()");
    }

    //initializing
    private void setupMonthAndYearAndList() {
        mCalendar.setTime(mDate);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mTransactions = getMonthlyTransactionList();
    }

    private ArrayList<Transaction> getMonthlyTransactionList () {
        Calendar calendar = new GregorianCalendar(mYear, mMonth, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Date fromDate = new GregorianCalendar(mYear, mMonth, 1, 0, 0, 0).getTime();
        Date toDate = new GregorianCalendar(mYear, mMonth, daysInMonth, 23,59, 59).getTime();

        return Transaction.filterTransactions(UserInfo.get(getApplicationContext()).getTransactions(),
                fromDate, toDate,
                MoneyTextWatcher.MIN_AMOUNT_LIMIT, MoneyTextWatcher.MAX_AMOUNT_LIMIT,
                "", "",
                Transaction.ALL_CATEGORY, Transaction.ALL_PRIORITY);
    }


    public void setDateButtonText() {
        String dateString = (String) DateFormat.format("yyyy MMM", mDate);
        mMonthButton.setText(dateString);
    }
    @Override
    public void updateDate(Date date) {
        mDate = date;
        setDateButtonText();
        setupMonthAndYearAndList();
        ((PriorityAdapter)getListAdapter()).notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary_priority, menu);
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
                Intent intent = new Intent(SummaryPriorityActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

    private class PriorityAdapter extends ArrayAdapter<String> {
        public PriorityAdapter(Context context, ArrayList<String> priorities) {
            super(context, 0, priorities);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(R.layout.summary_list_items, null);
            }

            String c = getItem(position);
            ImageView iconImageView = (ImageView) convertView.findViewById(R.id.summary_list_item_icon_ImageView);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.summary_list_item_title_TextView);
            TextView amountTextView = (TextView) convertView.findViewById(R.id.summary_list_item_total_amount_TextView);

            iconImageView.setImageResource
                    (getResources().getIdentifier(
                            Transaction.findPriorityIconResource(c), null, getPackageName()));
            titleTextView.setText(c);
            double amount = Transaction.TotalSumOfListByPriority(mTransactions,c);

            DecimalFormat df = new DecimalFormat("#.00");
            String formatted = "";
            if (amount > MoneyTextWatcher.MAX_DISPLAY_AMOUNT_LIMIT) {
                formatted = df.format(MoneyTextWatcher.MAX_DISPLAY_AMOUNT_LIMIT);
                formatted = "> " + formatted;
            } else if (amount < MoneyTextWatcher.MIN__DISPLAY_AMOUNT_LIMIT) {
                formatted = df.format(MoneyTextWatcher.MIN__DISPLAY_AMOUNT_LIMIT);
                formatted = "< " + formatted;

            } else if (amount == 0.0){
                formatted = "0.00";
            } else {
                formatted = df.format(amount);
            }
            amountTextView.setText(formatted);
            return convertView;
        }
    }


}
