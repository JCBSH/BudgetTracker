package com.mycompany.btrack.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.mycompany.btrack.LoginActivity;
import com.mycompany.btrack.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class LoginActivityTest
        extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity loginActivity;
    private EditText emailET, passwordET;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {

    }
    public void pre() {
        loginActivity = getActivity();
        emailET = (EditText) loginActivity.findViewById(R.id.email);
        passwordET = (EditText) loginActivity.findViewById(R.id.password);
    }
    public void testPreconditions() {
        pre();
        assertThat(loginActivity, notNullValue());
        assertThat(emailET, notNullValue());
        assertThat(passwordET, notNullValue());
    }
    @Test
    public void testLogin() throws Exception {
        assertThat(emailET.getText().toString(), equalTo(""));
        assertThat(passwordET.getText().toString(), equalTo(""));

    }

    @Test
    public void testSignUp() throws Exception {

    }

    @Test
    public void testRecover() throws Exception {

    }
}