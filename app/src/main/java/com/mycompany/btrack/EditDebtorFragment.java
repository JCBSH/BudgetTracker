package com.mycompany.btrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.mycompany.btrack.models.Transaction;
import com.mycompany.btrack.utils.StringTextWatcher;

/**
 * Created by JCBSH on 30/09/2015.
 */
public class EditDebtorFragment extends DialogFragment {
    public static final String EXTRA_NAME =
            "com.mycompany.btrack.EditDebtorFragment.name";

    private String mName;
    private String mOldName = "blank";
    private EditText mNameField;

    public void setOldName(String name) {
        mOldName = name;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_debtor,null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        mNameField = (EditText) v.findViewById(R.id.debtor_name_EditText);
        mNameField.addTextChangedListener(new StringTextWatcher(mNameField, Transaction.RECIPIENT_SIZE_LIMIT));
        Resources res = getResources();
        builder.setTitle(String.format(res.getString(R.string.edit_debtor_dialog_title), mOldName));
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mName = String.valueOf(mNameField.getText());
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditDebtorFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_NAME, mName);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }


}
