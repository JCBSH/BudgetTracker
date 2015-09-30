package com.mycompany.btrack.utils.tests;

import com.mycompany.btrack.utils.ErrorUtil;
import com.mycompany.btrack.utils.UserUtil;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserUtilTest {

    ErrorUtil error = new ErrorUtil();
    @Test
    public void testIsValidEmail() throws Exception {
//        assertFalse(UserUtil.isValidEmail(null, error));
//        assertFalse(UserUtil.isValidEmail("", error));
//        assertFalse(UserUtil.isValidEmail("abc", error));
//        assertFalse(UserUtil.isValidEmail("domain.com", error));
//        assertTrue(UserUtil.isValidEmail("a@mail.com", error));

    }

    @Test
    public void testIsValidPassword() throws Exception {
        assertFalse(UserUtil.isValidPassword(null, null, error));
        assertFalse(UserUtil.isValidPassword("", "b", error));
        assertFalse(UserUtil.isValidPassword("a", "b", error));
        assertFalse(UserUtil.isValidPassword("Password123", "password123", error));

        assertTrue(UserUtil.isValidPassword("aaaaaaaa", "aaaaaaaa", error));
        assertTrue(UserUtil.isValidPassword("Password123", "Password123", error));
    }

    @Test
    public void testIsValidPassword1() throws Exception {
        assertFalse(UserUtil.isValidPassword(null, error));
        assertFalse(UserUtil.isValidPassword("", error));
        assertFalse(UserUtil.isValidPassword("short", error));
        assertTrue(UserUtil.isValidPassword("password123", error));

    }
}