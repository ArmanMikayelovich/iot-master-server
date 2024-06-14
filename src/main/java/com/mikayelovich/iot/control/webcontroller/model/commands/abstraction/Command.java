package com.mikayelovich.iot.control.webcontroller.model.commands.abstraction;

public interface Command<T> {
    T mapToExecutable();
}
