package com.mycompany.btrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by JCBSH on 25/10/2015.
 */
public class MyAlertDialogFragment extends DialogFragment {
    private String mTitle;
    private String mMessage;
    public MyAlertDialogFragment(String title, String message) {
        super();
        mTitle = title;
        mMessage = message;


    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle(context.getString(R.string.error_title)).setMessage(context.getString(R.string.error_message));
        builder.setTitle(mTitle).setMessage(mMessage);
        builder.setPositiveButton("ok",null);

        AlertDialog dialog = builder.create();
        return dialog;


    }
}
