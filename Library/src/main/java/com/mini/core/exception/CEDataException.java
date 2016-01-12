package com.mini.core.exception;

import com.android.volley.VolleyError;

/**
 * Created by Wuquancheng on 15/4/2.
 */
public class CEDataException extends VolleyError {
    public static int ERROR_DATA_FORMAT = -10000;
    public static int ERROR_COMM = 1;

    private int code;
    private String message;

    public CEDataException(String detailMessage, Exception throwable) {
        super(detailMessage, throwable);
        this.message = detailMessage;
    }

    public CEDataException(int code, String detailMessage, Exception throwable) {
        super(detailMessage, throwable);
        this.message = detailMessage;
        this.code = code;
    }

    public CEDataException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        if (message == null) {
            message = super.getMessage();
            if (message == null) {
                message = super.getCause().getMessage();
            }
        }
        return message;
    }

    public String getMessage(String def) {
        if (message == null) {
            message = def;
        }
        return message;
    }
}
