package com.mikayelovich.iot.control.webcontroller.connectors;

import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.Command;

import java.io.Closeable;

public interface CommandPublisher<T> extends Closeable {

    boolean isActive();

    void sendCommand(Command<T> command);

}
