package com.mikayelovich.iot.control.webcontroller.model.dto.rest;

import lombok.Getter;

@Getter
public class EnablePinForDurationRequestDTO extends RequestDTO {
    private final  int pinNumber;
    private final long durationInSeconds;

    public EnablePinForDurationRequestDTO(String macAddress, int pinNumber, long durationInSeconds) {
        super(macAddress);
        this.pinNumber = pinNumber;
        this.durationInSeconds = durationInSeconds;
    }
}
