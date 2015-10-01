package com.mycompany.btrack.utils;

import com.mycompany.btrack.models.Debtor;

import java.util.Comparator;

/**
 * Created by JCBSH on 1/10/2015.
 */
public class DebtorComparator implements Comparator<Debtor> {
    @Override
    public int compare(Debtor o1, Debtor o2) {
        int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
        if (res == 0) {
            res = o1.getName().compareTo(o2.getName());
        }
        return res;
    }
}