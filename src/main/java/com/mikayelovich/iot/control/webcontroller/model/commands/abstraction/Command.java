package com.mikayelovich.iot.control.webcontroller.model.commands.abstraction;

interface Command<T> {
    T mapToExecutable();
}
