package com.mycompany.btrack.utils;

/**
 * Created by godfreytruong on 30/09/2015.
 */
public class ErrorUtil {
    private int code;
    private String message;

    public ErrorUtil() {
    }

    public ErrorUtil(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
