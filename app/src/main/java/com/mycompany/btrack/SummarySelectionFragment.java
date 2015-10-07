package com.mycompany.btrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

/**
 * Created by JCBSH on 7/10/2015.
 */
public class SummarySelectionFragment extends DialogFragment{
    public SummarySelectionFragment() {
        super();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_transaction_summary, null);

        builder.setTitle(R.string.transaction_summary_dialog_title);
        builder.setView(v)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SummarySelectionFragment.this.getDialog().cancel();
                    }
                });
        TextView categoryTextView = (TextView) v.findViewById(R.id.summary_Category_TextView_clicker);
        TextView priorityTextView = (TextView) v.findViewById(R.id.summary_priority_TextView_clicker);

        categoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SummaryCategoryActivity.class);
                startActivity(i);
            }
        });

        priorityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SummaryPriorityActivity.class);
                startActivity(i);
            }
        });
        return builder.create();
    }


}
