package com.mycompany.btrack.savedStates;

import android.content.Context;

/**
 * Created by JCBSH on 1/10/2015.
 */
public class HomeActivityTabState {
    private Context mAppContext;
    private static HomeActivityTabState sHomeActivityTabState;
    private int tabPosition;

    private HomeActivityTabState(Context appContext) {
        mAppContext = appContext;
        tabPosition = 0;
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
}
