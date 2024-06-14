package com.mikayelovich.iot.control.webcontroller.exceptions;

public class SocketClosedException extends RuntimeException {
    public SocketClosedException(String message) {
        super(message);
    }

    public SocketClosedException() {
    }
}
