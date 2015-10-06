package com.mycompany.btrack.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.btrack.R;
import com.mycompany.btrack.models.Transaction;

import java.util.ArrayList;

/**
 * Created by JCBSH on 7/10/2015.
 */

public class PriorityArrayAdaptor extends ArrayAdapter<String> {
    public PriorityArrayAdaptor(ArrayList<String> choices, Context context) {
        super(context, 0, choices);
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.list_item_drop_down, null);
        }
        String c = getItem(position);
        TextView label = (TextView) convertView.findViewById(R.id.list_item_drop_down_TextView);
        label.setText(c);

        ImageView icon = (ImageView) convertView.findViewById(R.id.list_item_drop_down_icon_ImageView);
        icon.setImageResource
                (getContext().getResources().getIdentifier(Transaction.findPriorityIconResource(c),
                        null, getContext().getPackageName()));

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.list_item_drop_down, null);
        }
        String c = getItem(position);
        TextView label = (TextView) convertView.findViewById(R.id.list_item_drop_down_TextView);
        label.setText(c);

        ImageView icon = (ImageView) convertView.findViewById(R.id.list_item_drop_down_icon_ImageView);
        icon.setImageResource
                (getContext().getResources().getIdentifier(Transaction.findPriorityIconResource(c),
                        null, getContext().getPackageName()));

        return convertView;
    }
}

