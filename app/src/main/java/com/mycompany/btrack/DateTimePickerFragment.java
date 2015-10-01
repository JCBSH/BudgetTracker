package com.mycompany.btrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by JCBSH on 4/03/2015.
 */
public class DateTimePickerFragment extends DialogFragment {

    public static final String TAG = DateTimePickerFragment.class.getSimpleName();
    public static final String EXTRA_DATE =
            "com.mycompany.btrack.DateTimePickerFragment.date";
    private Date mDate;
    private Callbacks mCallbacks;

    public static DateTimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE,date);
        DateTimePickerFragment fragment = new DateTimePickerFragment();
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
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date_time,null);
        //View container = v.findViewById(R.id.dialog_date_container);
        int width = 400;
        int height = 200;
        Log.d(TAG, String.valueOf(height));
        Log.d(TAG, String.valueOf(width));
        TimePicker timePicker = (TimePicker) v.findViewById(R.id.dialog_date_timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setIs24HourView(true);

        //timePicker.setVisibility(View.VISIBLE);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                mDate = new GregorianCalendar(year, month, day,hourOfDay, minute).getTime();
                // Update argument to preserve selected value on rotation
            }
        });
        DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);

        datePicker.init(year,month,day,new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                Log.d(TAG, String.valueOf(hour));
                int minute = calendar.get(Calendar.MINUTE);
                Log.d(TAG, String.valueOf(minute));
                // Translate year, month, day into a Date object using a calendar
                mDate = new GregorianCalendar(year, month, day, hour,minute).getTime();
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

