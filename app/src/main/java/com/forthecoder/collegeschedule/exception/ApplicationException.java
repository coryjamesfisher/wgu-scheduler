package com.forthecoder.collegeschedule.exception;

public class ApplicationException extends Exception {

    private final Throwable hiddenCause;

    public ApplicationException(String message, Throwable hiddenCause) {

        super(message);
        this.hiddenCause = hiddenCause;
    }
}
