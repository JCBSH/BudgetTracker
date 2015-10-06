package com.mycompany.btrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.mycompany.btrack.models.Debtor;
import com.mycompany.btrack.savedStates.HomeActivityTabState;
import com.mycompany.btrack.models.Transaction;


public class HomeActivity extends ActionBarActivity implements ActionBar.TabListener, TransactionFragment.Callbacks, DebtorFragment.Callbacks{

    public static final String TAG = HomeActivity.class.getSimpleName();
    private TabsAdapter mTabsAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setProgressBarIndeterminateVisibility(true);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabsAdapter = new TabsAdapter(this,getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.homePage);
        mViewPager.setAdapter(mTabsAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });


        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mTabsAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mTabsAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        int position = HomeActivityTabState.get(getApplicationContext()).getTabPosition();
        Log.d(TAG, String.format("Restore position %d", position));
        getSupportActionBar().setSelectedNavigationItem(position);
        mViewPager.setCurrentItem( getSupportActionBar().getSelectedTab().getPosition());

        Log.d(TAG, "onCreate()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            App app = (App) getApplicationContext();
            app.getFirebase().unauth();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        HomeActivityTabState.get(getApplicationContext()).setTabPosition(getSupportActionBar().getSelectedTab().getPosition());
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onTransactionSelected(Transaction transaction) {
//        Intent i = new Intent(this, EditTransactionActivity.class);
//        i.putExtra(EditTransactionActivity.EXTRA_TRANSACTION_ID, transaction.getId());
//        startActivity(i);
    }

    @Override
    public void onDebtorSelected(Debtor debtor) {
        Intent i = new Intent(this, DebtActivity.class);
        i.putExtra(DebtorFragment.EXTRA_DEBTOR_NAME, debtor.getName());
        startActivity(i);
    }
}
