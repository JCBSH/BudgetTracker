package com.mycompany.btrack.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.mycompany.btrack.DebtActivity;
import com.mycompany.btrack.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.NotNull;


import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by Jaan on 3/10/2015.
 */
public class DebtActivityTest
        extends ActivityInstrumentationTestCase2<DebtActivity> {

    private DebtActivity debtActivity;
    private ;

    public DebtActivityTest(Class<DebtActivity> activityClass) {    super(activityClass);   }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void pre(){

        debtActivity = getActivity();

    }

    public void testPreconditions(){

        pre();
        assertThat(debtActivity, notNullValue());


    }
}