package com.mikayelovich.iot.control.model.commands.abstraction;

public interface Command<T> {
    T mapToExecutable();

    //this is unique identifier of the IOT device, whom we're going to send them message,
    // it can be MAC address of the device, or the unique topic name,dedicated for that device
    String getUniqueId();
}
