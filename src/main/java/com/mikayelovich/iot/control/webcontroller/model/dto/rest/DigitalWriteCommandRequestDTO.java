package com.mikayelovich.iot.control.webcontroller.model.dto.rest;

import com.mikayelovich.iot.control.webcontroller.model.commands.DigitalWriteState;
import lombok.Getter;

@Getter
public class DigitalWriteCommandRequestDTO extends RequestDTO {
    private final int pinNumber;
    private final DigitalWriteState state;

    public DigitalWriteCommandRequestDTO(String macAddress, int pinNumber, DigitalWriteState state) {
        super(macAddress);
        this.pinNumber = pinNumber;
        this.state = state;
    }
}
