package com.mycompany.btrack.utils.tests;

import com.mycompany.btrack.utils.ErrorUtil;
import com.mycompany.btrack.utils.TransactionUtil;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TransactionUtilTest {

    @Test
    public void testIsValidAmount() throws Exception {
        ErrorUtil error = new ErrorUtil();
        assertFalse(error.getMessage(), TransactionUtil.isValidAmount(null, error));
        assertFalse(error.getMessage(), TransactionUtil.isValidAmount("", error));
        assertFalse(error.getMessage(), TransactionUtil.isValidAmount("abc", error));
        assertFalse(error.getMessage(), TransactionUtil.isValidAmount("1-", error));
        assertFalse(error.getMessage(), TransactionUtil.isValidAmount("-1-", error));
        assertFalse(error.getMessage(), TransactionUtil.isValidAmount("1.111", error));
        assertFalse(error.getMessage(), TransactionUtil.isValidAmount("-", error));
        assertTrue(error.getMessage(), TransactionUtil.isValidAmount("1", error));
        assertTrue(error.getMessage(), TransactionUtil.isValidAmount("1.", error));
        assertTrue(error.getMessage(), TransactionUtil.isValidAmount("-1.1", error));
        assertTrue(error.getMessage(), TransactionUtil.isValidAmount("99.01", error));
        assertTrue(error.getMessage(), TransactionUtil.isValidAmount(".99", error));
        assertTrue(error.getMessage(), TransactionUtil.isValidAmount("-.99", error));
    }

    @Test
    public void testIsValidRecipient() throws Exception {
        ErrorUtil error = new ErrorUtil();
        assertFalse(TransactionUtil.isValidRecipient("very long recipient is very long", error));
        assertFalse(TransactionUtil.isValidRecipient(null, error));
        assertTrue(TransactionUtil.isValidRecipient("", error));
        assertTrue(TransactionUtil.isValidRecipient("UNSW", error));
    }

    @Test
    public void testIsValidDescription() throws Exception {
        ErrorUtil error = new ErrorUtil();
        char[] chars = new char[300];
        Arrays.fill(chars, 'a');
        assertFalse(TransactionUtil.isValidDescription(new String(chars), error));
        assertFalse(TransactionUtil.isValidDescription(null, error));
        assertTrue(TransactionUtil.isValidDescription("", error));
        assertTrue(TransactionUtil.isValidDescription("course fees", error));
    }
}