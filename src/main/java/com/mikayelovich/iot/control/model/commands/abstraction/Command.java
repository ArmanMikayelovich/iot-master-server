package com.mikayelovich.iot.control.model.commands.abstraction;

public interface Command<T> {
    T mapToExecutable();
}
