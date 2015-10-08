package com.mycompany.btrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by JCBSH on 7/10/2015.
 */
public class MonthPickerFragment extends DialogFragment {

    public static final String TAG = MonthPickerFragment.class.getSimpleName();
    public static final String EXTRA_DATE =
            "com.mycompany.btrack.MonthPickerFragment.date";
    private Date mDate;
    private Callbacks mCallbacks;

    public static MonthPickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE,date);
        MonthPickerFragment fragment = new MonthPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = 1;

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_month,null);

        DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_month_datePicker);

        datePicker.findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        datePicker.init(year,month,day,new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                // Translate year, month, day into a Date object using a calendar
                mDate = new GregorianCalendar(year, month, day).getTime();
                // Update argument to preserve selected value on rotation
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.date_picker_title);
        builder.setView(v);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCallbacks.updateDate(mDate);
            }
        });
        return builder.create();

    }



    public interface Callbacks {
        void updateDate(Date date);
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


}


