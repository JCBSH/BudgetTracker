package com.mycompany.btrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.mycompany.btrack.models.Transaction;
import com.mycompany.btrack.utils.CategoryArrayAdaptor;
import com.mycompany.btrack.utils.MoneyTextWatcher;
import com.mycompany.btrack.utils.PriorityArrayAdaptor;
import com.mycompany.btrack.utils.StringTextWatcher;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by JCBSH on 30/09/2015.
 */
public class FilterTransactionFragment extends DialogFragment {
    public static final String TAG = FilterTransactionFragment.class.getSimpleName();
    public static final String EXTRA_FROM_DATE =
            "com.mycompany.btrack.DateTimePickerFragment.fromDate";
    public static final String EXTRA_TO_DATE =
            "com.mycompany.btrack.DateTimePickerFragment.toDate";
    public static final String EXTRA_RECIPIENT =
            "com.mycompany.btrack.DateTimePickerFragment.recipient";
    public static final String EXTRA_AMOUNT_FROM =
            "com.mycompany.btrack.DateTimePickerFragment.amountFrom";
    public static final String EXTRA_AMOUNT_TO =
            "com.mycompany.btrack.DateTimePickerFragment.amountTo";
    public static final String EXTRA_DESCRIPTION =
            "com.mycompany.btrack.DateTimePickerFragment.description";
    public static final String EXTRA_CATEGORY =
            "com.mycompany.btrack.DateTimePickerFragment.category";
    public static final String EXTRA_PRIORITY =
            "com.mycompany.btrack.DateTimePickerFragment.priority";


    private EditText mRecipientField;
    private EditText mAmountFromField;
    private EditText mAmountToField;
    private EditText mDescriptionPhaseField;
    private Date mFromDate;
    private Date mToDate;
    private double mAmountFrom;
    private double mAmountTo;
    private String mRecipient;
    private String mDescription;
    private Spinner mCategorySpinner;
    private Spinner mPrioritySpinner;

    public FilterTransactionFragment() {
        super();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_transaction_filter,null);
        RelativeLayout pickerContainer = (RelativeLayout) v.findViewById(R.id.transaction_filter_picker_container);
        View pickersView = getActivity().getLayoutInflater().inflate(R.layout.transaction_filter_pickers, pickerContainer);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        mRecipientField = (EditText) v.findViewById(R.id.transaction_filter_recipient_EditText);
        mRecipientField.addTextChangedListener(new StringTextWatcher(mRecipientField, Transaction.RECIPIENT_SIZE_LIMIT));
        mAmountFromField = (EditText) v.findViewById(R.id.transaction_filter_amount_from_EditText);
        mAmountFromField.addTextChangedListener(new MoneyTextWatcher(mAmountFromField));
        mAmountToField = (EditText) v.findViewById(R.id.transaction_filter_amount_to_EditText);
        mAmountToField.addTextChangedListener(new MoneyTextWatcher(mAmountToField));
        mDescriptionPhaseField = (EditText) v.findViewById(R.id.transaction_filter_description_EditText);
        mDescriptionPhaseField.addTextChangedListener(new StringTextWatcher(mDescriptionPhaseField, Transaction.DESCRIPTION_SIZE_LIMIT));

        DatePicker fromPicker = (DatePicker) pickersView.findViewById(R.id.from_DatePicker);
        DatePicker toPicker = (DatePicker) pickersView.findViewById(R.id.to_DatePicker);

        Serializable fromDateSerializable = getArguments().getSerializable(TransactionFragment.FILTER_FROM_DATE_BUNDLE_KEY);
        Serializable toDateSerializable = getArguments().getSerializable(TransactionFragment.FILTER_TO_DATE_BUNDLE_KEY);
        mAmountFrom = getArguments().getDouble(TransactionFragment.FILTER_AMOUNT_FROM_BUNDLE_KEY, MoneyTextWatcher.MIN_AMOUNT);
        mAmountTo = getArguments().getDouble(TransactionFragment.FILTER_AMOUNT_TO_BUNDLE_KEY, MoneyTextWatcher.MAX_AMOUNT);
        mRecipient = getArguments().getString(TransactionFragment.FILTER_RECIPIENT_BUNDLE_KEY);
        mDescription = getArguments().getString(TransactionFragment.FILTER_DESCRIPTION_BUNDLE_KEY);

        Log.d(TAG, "amount from " + String.valueOf(mAmountFrom));
        Log.d(TAG, "amount to " + String.valueOf(mAmountTo));
        Log.d(TAG,  "recipient " + mRecipient);
        Log.d(TAG,  "description " + mDescription);

        if (mAmountFrom != MoneyTextWatcher.MIN_AMOUNT) {
            DecimalFormat df = new DecimalFormat("#.00");
            String formatted = df.format(mAmountFrom);
            mAmountFromField.setText(formatted);
        }

        if (mAmountTo !=  MoneyTextWatcher.MAX_AMOUNT) {
            DecimalFormat df = new DecimalFormat("#.00");
            String formatted = df.format(mAmountTo);
            mAmountToField.setText(formatted);
            //Log.d(TAG, "amount to in field " + String.valueOf(mAmountToField.getText()));
        }

        if (mRecipient != null) {
            mRecipientField.setText(mRecipient);
        }

        if (mDescription != null) {
            mDescriptionPhaseField.setText(mDescription);
        }

        Calendar calendar = Calendar.getInstance();

        if (fromDateSerializable == null) {
            mFromDate = new Date();
            Log.d(TAG, "didn't pass from date");
        } else {
            mFromDate = (Date) fromDateSerializable;
            Log.d(TAG, "have passed from date");
//            calendar.setTime(mFromDate);
//            int fromYear = calendar.get(Calendar.YEAR);
//            int fromMonth = calendar.get(Calendar.MONTH);
//            int fromDay = calendar.get(Calendar.DAY_OF_MONTH);
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            int minute = calendar.get(Calendar.MINUTE);
//            Log.d(TAG, String.format("year %d", fromYear));
//            Log.d(TAG, String.format("month %d", fromMonth));
//            Log.d(TAG, String.format("day %d", fromDay));
//            Log.d(TAG, String.format("hour %d", hour));
//            Log.d(TAG, String.format("minutes %d", minute));
        }

        calendar.setTime(mFromDate);
        int fromYear = calendar.get(Calendar.YEAR);
        int fromMonth = calendar.get(Calendar.MONTH);
        int fromDay = calendar.get(Calendar.DAY_OF_MONTH);


        if (toDateSerializable == null) {
            mToDate = new Date();
            Log.d(TAG, "didn't pass to date");
        } else {
            mToDate = (Date) toDateSerializable;
            Log.d(TAG, "have passed to date");
//            calendar.setTime(mToDate);
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            int minute = calendar.get(Calendar.MINUTE);
//            Log.d(TAG, String.format("year %d", year));
//            Log.d(TAG, String.format("month %d", month));
//            Log.d(TAG, String.format("day %d", day));
//            Log.d(TAG, String.format("hour %d", hour));
//            Log.d(TAG, String.format("minutes %d", minute));
        }

        calendar.setTime(mToDate);
        int toYear = calendar.get(Calendar.YEAR);
        int toMonth = calendar.get(Calendar.MONTH);
        int toDay = calendar.get(Calendar.DAY_OF_MONTH);


        fromPicker.init(fromYear,fromMonth,fromDay,new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                int hour = 0;
                int minute = 0;
                // Translate year, month, day into a Date object using a calendar
                mFromDate = new GregorianCalendar(year, month, day, hour,minute, 0).getTime();
            }
        });

        toPicker.init(toYear,toMonth,toDay,new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                int hour = 23;
                int minute = 59;
                // Translate year, month, day into a Date object using a calendar
                mToDate = new GregorianCalendar(year, month, day, hour,minute, 59).getTime();
            }
        });



        builder.setTitle(R.string.transaction_filter_dialog_title);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FilterTransactionFragment.this.getDialog().cancel();
                    }
                });

        mCategorySpinner = (Spinner) v.findViewById(R.id.transaction_filter_category_spinner);
        mPrioritySpinner = (Spinner) v.findViewById(R.id.transaction_filter_priority_spinner);
        ArrayList<String> categoryChoices = Transaction.getCategoryChoicesForFilter();
        ArrayAdapter<String> cAdapter = new CategoryArrayAdaptor(categoryChoices, getActivity());
        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(cAdapter);
        mCategorySpinner.setSelection(categoryChoices.indexOf(getArguments().
                getString(TransactionFragment.FILTER_CATEGORY_BUNDLE_KEY)));
       // mCategorySpinner.setSelection(categoryChoices.indexOf(mTransaction.getCategory()));

        ArrayList<String> priorityChoices = Transaction.getPriorityChoicesForFilter();
        ArrayAdapter<String> pAdapter = new PriorityArrayAdaptor(priorityChoices, getActivity());
        // Apply the adapter to the spinner
        mPrioritySpinner.setAdapter(pAdapter);
        mPrioritySpinner.setSelection(priorityChoices.indexOf(getArguments().
                getString(TransactionFragment.FILTER_PRIORITY_BUNDLE_KEY)));
       // mPrioritySpinner.setSelection(priorityChoices.indexOf(mTransaction.getPriority()));
        return builder.create();
    }

    private void sendResult(int resultCode) {
//        FilterTransactionFragment.this.getDialog().cancel();
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        String amountFromString = String.valueOf(mAmountFromField.getText());
        if (amountFromString.equals("") || amountFromString.equals("-") || amountFromString.equals(".")) {
            mAmountFrom = MoneyTextWatcher.MIN_AMOUNT;
        } else {
            mAmountFrom =  Double.parseDouble(String.valueOf(mAmountFromField.getText()));
        }

        String amountToString = String.valueOf(mAmountToField.getText());
        if (amountToString.equals("") || amountToString.equals("-") || amountToString.equals(".")) {
            //i.putExtra(EXTRA_AMOUNT_TO, Double.parseDouble(String.valueOf(mAmountToField.getText())));
            mAmountTo = MoneyTextWatcher.MAX_AMOUNT;
        } else {
            mAmountTo = Double.parseDouble(String.valueOf(mAmountToField.getText()));
        }

        if (mAmountFrom > mAmountTo) {
            double temp = mAmountFrom;
            mAmountFrom = mAmountTo;
            mAmountTo = temp;
        }
        i.putExtra(EXTRA_AMOUNT_FROM, mAmountFrom);
        i.putExtra(EXTRA_AMOUNT_TO, mAmountTo);
        i.putExtra(EXTRA_RECIPIENT, String.valueOf(mRecipientField.getText()));
        i.putExtra(EXTRA_DESCRIPTION, String.valueOf(mDescriptionPhaseField.getText()));

        if (mToDate.after(mFromDate)) {
            i.putExtra(EXTRA_FROM_DATE, mFromDate);
            i.putExtra(EXTRA_TO_DATE, mToDate);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mFromDate);
            int newToYear = calendar.get(Calendar.YEAR);
            int newToMonth = calendar.get(Calendar.MONTH);
            int newToDay = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.setTime(mToDate);
            int newFromYear = calendar.get(Calendar.YEAR);
            int newFromMonth = calendar.get(Calendar.MONTH);
            int newFromDay = calendar.get(Calendar.DAY_OF_MONTH);

            mFromDate = new GregorianCalendar(newFromYear, newFromMonth, newFromDay, 0,0, 0).getTime();
            i.putExtra(EXTRA_FROM_DATE, mFromDate);

            mToDate = new GregorianCalendar(newToYear, newToMonth, newToDay, 23,59, 59).getTime();
            i.putExtra(EXTRA_TO_DATE, mToDate);

        }

        i.putExtra(EXTRA_CATEGORY, (String) mCategorySpinner.getSelectedItem());
        i.putExtra(EXTRA_PRIORITY, (String) mPrioritySpinner.getSelectedItem());
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

}
