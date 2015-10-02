package com.mycompany.btrack.savedStates;

import android.content.Context;

/**
 * Created by JCBSH on 2/10/2015.
 */
public class DebtActivityState {
    private Context mAppContext;
    private static DebtActivityState sDebtActivityState;
    private String debtorName;

    private DebtActivityState(Context appContext) {
        mAppContext = appContext;
    }

    public static DebtActivityState get(Context c) {
        if (sDebtActivityState == null) {
            sDebtActivityState = new DebtActivityState(c.getApplicationContext());
        }
        return sDebtActivityState;
    }


    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }
}
