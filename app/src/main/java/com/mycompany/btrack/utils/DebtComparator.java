package com.mycompany.btrack.utils;

import com.mycompany.btrack.models.Debt;

import java.util.Comparator;

/**
 * Created by JCBSH on 2/10/2015.
 */

public class DebtComparator implements Comparator<Debt> {
    @Override
    public int compare(Debt o1, Debt o2) {
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