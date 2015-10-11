package com.mycompany.btrack;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.btrack.models.Debt;
import com.mycompany.btrack.models.Debtor;
import com.mycompany.btrack.models.UserInfo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DebtorFragment extends ListFragment {

    public static final String TAG = DebtorFragment.class.getSimpleName();

    public static final String EXTRA_DEBTOR_NAME = "debtor name";

    private static final String EDIT_NAME = "edit name";
    private static final int REQUEST_NAME = 0;

    private ArrayList<Debtor> mDebtors;
    private ArrayList<Debtor> mDeleteDebtorsList;
    private ArrayList<Integer> mDeleteListPosition;
    private ImageButton mAddCancelButton;
    private Callbacks mCallbacks;
    private boolean mDeleteStatus;
    private String mCurrentEditName;
    private Debtor mCurrentSelectedDebtor;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onDebtorSelected(Debtor debtor);
    }

    public DebtorFragment() {
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            //getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mDeleteStatus = true;
            mAddCancelButton.setImageResource(R.drawable.close);
            ((DebtorAdapter)getListAdapter()).notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_debtor_menu, menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        mCurrentEditName = "";
        mDeleteStatus = false;

        mDebtors = UserInfo.get(getActivity().getApplicationContext()).getDebtors();
        mDeleteDebtorsList = new ArrayList<Debtor>();
        mDeleteListPosition =  new ArrayList<Integer>();
        DebtorAdapter adapter = new DebtorAdapter(mDebtors);
        setListAdapter(adapter);

        Log.d(TAG, "onCreate()");
        debtorFragment = this;
    }
    static DebtorFragment debtorFragment;
    public static void refresh() {
        ((DebtorAdapter) debtorFragment.getListAdapter()).notifyDataSetChanged();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_debtor,
                container, false);
//
//
        //ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        mAddCancelButton = (ImageButton) rootView.findViewById(R.id.debtor_AddCancelButton);
        mAddCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteStatus == false) {
                    //mCurrentSelectedDebtor = new Debtor();
                    //UserInfo.get(getActivity().getApplicationContext()).addDebtor(mCurrentSelectedDebtor);
                    mCurrentSelectedDebtor = UserInfo.get(getActivity().getApplicationContext()).createAndAddNewDebtor();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    //DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                    //TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                    EditDebtorFragment editFragment = new EditDebtorFragment();
                    editFragment.setOldName(mCurrentSelectedDebtor.getName());
                    editFragment.setTargetFragment(DebtorFragment.this, REQUEST_NAME);
                    //Log.d(TAG, "before");
                    editFragment.show(fm, EDIT_NAME);
                    //Log.d(TAG, "return");
                    UserInfo.get(getActivity().getApplicationContext()).sortDebtors();
                    UserInfo.get(getActivity().getApplicationContext()).saveUserInfo();
                    ((DebtorAdapter) getListAdapter()).notifyDataSetChanged();

                } else {
                    mAddCancelButton.setImageResource(R.drawable.add);
                    mDeleteStatus = false;
                    ((DebtorAdapter) getListAdapter()).notifyDataSetChanged();
                }
            }
        });


        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
        Log.d(TAG, "onAttach()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DebtorAdapter)getListAdapter()).notifyDataSetChanged();
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
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        Log.d(TAG, "onDetach()");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult()");
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_NAME:
                mCurrentEditName = (String) data.getSerializableExtra(EditDebtorFragment.EXTRA_NAME);
                if (mCurrentEditName.equalsIgnoreCase("")) {
                    //Log.d(TAG,"skipped");
                    break;
                }
                //Log.d(TAG,"no skipped");
                if(UserInfo.get(getActivity().getApplicationContext()).
                        changeName(mCurrentSelectedDebtor, mCurrentEditName) == true) {
                    UserInfo.get(getActivity().getApplicationContext()).sortDebtors();
                    UserInfo.get(getActivity().getApplicationContext()).saveUserInfo();
                    ((DebtorAdapter) getListAdapter()).notifyDataSetChanged();
                } else {
                    Toast toast = Toast.makeText(getActivity(),getString(R.string.debtor_name_exist_message), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Log.d(TAG, "onListItemClick()");
        super.onListItemClick(l, v, position, id);
        //Log.d(TAG, "onListItemClick() 2");
        Debtor t = ((DebtorAdapter)getListAdapter()).getItem(position);
        mCallbacks.onDebtorSelected(t);




    }


    private class DebtorAdapter extends ArrayAdapter<Debtor> {
        public DebtorAdapter(ArrayList<Debtor> debtors) {
            super(getActivity(), 0, debtors);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.debtor_list_item, null);
            }
            final Debtor c = getItem(position);


            TextView recipientTextView =
                    (TextView)convertView.findViewById(R.id.debtor_list_item_name_TextView);
            recipientTextView.setText(c.getName());

            TextView balanceTextView =
                    (TextView)convertView.findViewById(R.id.debtor_list_item_amount_TextView);

            double balance = c.getBalance();
            if (balance > 0) {
                balanceTextView.setTextColor(Color.parseColor(getString(R.string.positive_green)));
            } else if (balance < 0) {
                balanceTextView.setTextColor(Color.parseColor(getString(R.string.negative_red)));
            } else {
                balanceTextView.setTextColor(Color.BLACK);
            }

            balanceTextView.setText(c.getFormatBalance());



            ImageButton button = (ImageButton) convertView.findViewById(R.id.debtor_editDeleteButton);
            if (mDeleteStatus == true) {
                button.setImageResource(R.drawable.smallbin);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setTitle("Delete Debtor")
                                .setMessage("Are you sure you want to delete " + c.getName() + "?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        UserInfo.get(getActivity().getApplicationContext()).deleteDebtor(c);
                                        mDebtors.remove(c);

                                        UserInfo.get(getActivity().getApplicationContext()).saveUserInfo();
                                        ((DebtorAdapter) getListAdapter()).notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            } else {
                button.setImageResource(R.drawable.edit);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCurrentSelectedDebtor = c;
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        EditDebtorFragment editFragment = new EditDebtorFragment();
                        editFragment.setOldName(mCurrentSelectedDebtor.getName());
                        editFragment.setTargetFragment(DebtorFragment.this, REQUEST_NAME);
                        //Log.d(TAG, "before");
                        editFragment.show(fm, EDIT_NAME);
                    }
                });
            }



            return convertView;
        }
    }
}
