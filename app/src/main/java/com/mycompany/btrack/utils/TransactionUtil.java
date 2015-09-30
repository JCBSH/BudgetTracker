package com.mycompany.btrack.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by godfreytruong on 30/09/2015.
 */
public class TransactionUtil {

    private static final int RECIPIENT_LIMIT = 20;
    private static final int DESCRIPTION_LIMIT = 50;
    private static TransactionUtil instance = null;

    private TransactionUtil() {
        // singleton class
    }
    public static TransactionUtil getInstance() {
        if (instance == null) {
            instance = new TransactionUtil();
        }
        return instance;
    }

    public static final boolean isValidAmount(String amount, ErrorUtil error) {
        try {
            Double.parseDouble(amount);
            String[] digits = amount.split("\\.");
            if (digits.length == 0) {
//                error.setMessage("amount is empty");
//                error.setCode(2);
//                return false;
                return true;
            } else if (digits.length == 1) {
                if (digits[0].length() == 0) {
//            error.setMessage("amount is empty");
//            error.setCode(2);
//            return false;
                    return true;
                } else if (digits[0].charAt(0) == '-') {
                    if (digits[0].length() == 1) {
                        error.setMessage("amount is empty");
                        error.setCode(2);
                        return false;
                    } else if (digits[0].length() > 9) {
                        error.setMessage("amount has more than 10 digits");
                        return false;
                    }
                } else if (digits[0].length() > 10) {
                    error.setMessage("amount has more than 10 digits");
                    return false;
                }
            } else if (digits.length == 2) {
                if (digits[0].length() == 0) {
    //            error.setMessage("amount is empty");
    //            error.setCode(2);
    //            return false;
                    return true;
                } else if (digits[0].charAt(0) == '-') {
                    if (digits[0].length() == 1) {
    //                        error.setMessage("amount is empty");
    //                        error.setCode(2);
    //                        return false;
                    } else if (digits[0].length() > 9) {
                        error.setMessage("amount has more than 10 digits");
                        return false;
                    }
                } else if (digits[0].length() > 10) {
                    error.setMessage("amount has more than 10 digits");
                    return false;
                }
                if (digits[1].length() > 2) {
                    error.setMessage("amount has more than 2 decimal places");
                    return false;
                }
            } else if (digits.length > 2) {
                error.setMessage("amount has more than 1 decimal point");
                return false;
            }
//            digits = amount.split("-");
//            if (digits.length > 1) {
//                error.setMessage("amount is not a number");
//                return false;
//            } else if (digits.length == 1 && digits[0].charAt(0) != '-') {
//                error.setMessage("amount is not a number");
//                return false;
//            }
        } catch (NullPointerException e) {
            error.setMessage("Null amount");
            error.setCode(0);
            return false;
        } catch (NumberFormatException e) {
            error.setMessage("amount is not a number");
            error.setCode(1);
            return false;
        }
        return true;
    }

    public static final boolean isValidRecipient(String recipient, ErrorUtil error) {
        if (recipient == null) {
            return false;
        } else if (recipient.length() > RECIPIENT_LIMIT) {
            error.setMessage("recipient is more than " + RECIPIENT_LIMIT + " characters");
            return false;
        }
        return true;
    }
    public static final boolean isValidDescription(String description, ErrorUtil error) {
        if (description == null) {
            return false;
        } else if (description.length() > DESCRIPTION_LIMIT) {
            error.setMessage("description is more than " + DESCRIPTION_LIMIT + " characters");
            return false;
        }
        return true;
    }
}
