package com.mycompany.btrack.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import com.mycompany.btrack.MyAlertDialogFragment;
import com.mycompany.btrack.R;

/**
 * Created by JCBSH on 25/10/2015.
 */
public class InternetUtil {
    public static void alertUserNetwork(Activity activity) {
        MyAlertDialogFragment dialog = new MyAlertDialogFragment(activity.getString(R.string.error_network_title),
                activity.getString(R.string.error_network_message));
        dialog.show(activity.getFragmentManager(),"blah");
    }

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()) {
            isConnected = true;
        }
        return isConnected;
    }

}
