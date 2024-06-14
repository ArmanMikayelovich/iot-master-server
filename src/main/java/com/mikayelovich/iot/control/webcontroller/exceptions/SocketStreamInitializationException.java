package com.mikayelovich.iot.control.webcontroller.exceptions;

public class SocketStreamInitializationException extends RuntimeException {
    public SocketStreamInitializationException(String message) {
        super(message);
    }

    public SocketStreamInitializationException() {
    }
}
