package com.ysg;

public enum ErrorInfo {
    OK(0),
    LENGTH_TOO_LONG(1);
    private final  int code;

    ErrorInfo(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
