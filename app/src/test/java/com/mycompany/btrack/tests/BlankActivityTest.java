package com.mycompany.btrack.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.mycompany.btrack.BlankActivity;
import com.mycompany.btrack.R;

import org.junit.After;
import org.junit.Before;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class BlankActivityTest
        extends ActivityInstrumentationTestCase2<BlankActivity> {

    private BlankActivity activity;
    private TextView textView;

    public BlankActivityTest() {
        super(BlankActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
//        activity = getActivity();
//        textView = (TextView) activity.findViewById(R.id.hello);
//        assertNotNull(activity);
    }
    public void pre() {
        activity = getActivity();
        assertThat(activity, notNullValue());
        textView = (TextView) activity.findViewById(R.id.hello);
    }
    @After
    public void tearDown() throws Exception {

    }

    public void testSomething() throws Exception {
        pre();
        String actual = textView.getText().toString();
        String expected = activity.getString(R.string.hello_world);
        assertThat(expected, equalTo(actual));

    }
}