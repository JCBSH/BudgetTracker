package com.mycompany.btrack.utils;

import com.mycompany.btrack.models.Transaction;

import java.util.Comparator;

/**
 * Created by JCBSH on 1/10/2015.
 */
public class TransactionComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction o1, Transaction o2) {
        int i = o1.getDate().compareTo(o2.getDate());
        if (i == 1) {
            return -1;
        } else if (i == -1) {
            return 1;
        } else {
            return 0;
        }
    }
}