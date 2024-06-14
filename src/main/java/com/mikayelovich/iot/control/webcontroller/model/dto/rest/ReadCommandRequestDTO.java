package com.mikayelovich.iot.control.webcontroller.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ReadCommandRequestDTO extends RequestDTO {
    private final int pinNumber;

    public ReadCommandRequestDTO(String macAddress, int pinNumber) {
        super(macAddress);
        this.pinNumber = pinNumber;
    }
}
