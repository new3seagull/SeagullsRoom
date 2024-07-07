package com.new3seagull.SeagullsRoom.global.error;

public class ExceptionResponse {

    private int value;
    private String message;

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public ExceptionResponse(int value, String message) {
        this.value = value;
        this.message = message;
    }
}
