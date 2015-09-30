package com.mycompany.btrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by JCBSH on 30/09/2015.
 */
public class FilterTransactionFragment extends DialogFragment {
    private String mName;
    private Button mFromDateButton;
    private Button mToDateButton;
    private EditText mRecipientField;
    private EditText mAmountField;
    private EditText mDescriptionPhaseField;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_transaction_filter,null);
        RelativeLayout pickerContainer = (RelativeLayout) v.findViewById(R.id.transaction_filter_picker_container);
        View pickersView = getActivity().getLayoutInflater().inflate(R.layout.transaction_filter_pickers, pickerContainer);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        //mNameField = (EditText) v.findViewById(R.id.debtor_name_EditText);
        //mNameField.addTextChangedListener(new StringTextWatcher(mNameField, 20));;
        //mFromDateButton = (Button) v.findViewById(R.id.transaction_filter_from_button);
        //mToDateButton = (Button) v.findViewById(R.id.transaction_filter_to_button);
        mRecipientField = (EditText) v.findViewById(R.id.transaction_filter_recipient_EditText);
        mAmountField = (EditText) v.findViewById(R.id.transaction_filter_amount_EditText);
        mDescriptionPhaseField = (EditText) v.findViewById(R.id.transaction_filter_description_EditText);
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
        return builder.create();
    }

    private void sendResult(int resultCode) {
        FilterTransactionFragment.this.getDialog().cancel();
//        if (getTargetFragment() == null)
//            return;
//        Intent i = new Intent();
//        i.putExtra(EXTRA_NAME, mName);
//        getTargetFragment()
//                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

}
