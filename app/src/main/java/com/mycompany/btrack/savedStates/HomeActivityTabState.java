package com.mycompany.btrack.savedStates;

import android.content.Context;

import com.mycompany.btrack.R;

/**
 * Created by JCBSH on 1/10/2015.
 */
public class HomeActivityTabState {
    private Context mAppContext;
    private static HomeActivityTabState sHomeActivityTabState;
    private int tabPosition;
    private String transactionFragmentTitle;
    private String debtorFragmentTitle;

    private HomeActivityTabState(Context appContext) {
        mAppContext = appContext;
        tabPosition = 0;
        transactionFragmentTitle = appContext.getString(R.string.TransactionFragment_all_transaction_title);
        debtorFragmentTitle = appContext.getString(R.string.DebtorFragment_all_debtor_title);
    }

    public static HomeActivityTabState get(Context c) {
        if (sHomeActivityTabState == null) {
            sHomeActivityTabState = new HomeActivityTabState(c.getApplicationContext());
        }
        return sHomeActivityTabState;
    }


    public int getTabPosition() {
        return tabPosition;
    }

    public void setTabPosition(int tabPosition) {
        this.tabPosition = tabPosition;
    }

    public String getTransactionFragmentTitle() {
        return transactionFragmentTitle;
    }

    public void setTransactionFragmentTitle(String transactionFragmentTitle) {
        this.transactionFragmentTitle = transactionFragmentTitle;
    }

    public String getDebtorFragmentTitle() {
        return debtorFragmentTitle;
    }

    public void setDebtorFragmentTitle(String debtorFragmentTitle) {
        this.debtorFragmentTitle = debtorFragmentTitle;
    }

    public String getTabTitle(int position) {
        if (position == 0) {
            return getTransactionFragmentTitle();
        } else {
            return getDebtorFragmentTitle();
        }
    }
}
