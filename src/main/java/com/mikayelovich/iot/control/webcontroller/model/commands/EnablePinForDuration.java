package com.mikayelovich.iot.control.webcontroller.model.commands;

import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.webcontroller.model.commands.enums.DigitalWriteState;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.EnablePinForDurationRequestDTO;
import com.mikayelovich.iot.control.webcontroller.model.microcontrontroller.esp32.Esp32Devkit1Pin;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EnablePinForDuration extends StringExecutableCommand {
    private final static String ENABLE_PIN_FOR = "enablePinFor";

    private final Esp32Devkit1Pin pinNumber;

    private final long durationInSeconds;


    public EnablePinForDuration(Esp32Devkit1Pin pinNumber, Duration duration) {
        this.pinNumber = pinNumber;
        this.durationInSeconds = duration.toSeconds();
    }

    @Override
    protected String getCommandName() {
        return ENABLE_PIN_FOR;
    }

    @Override
    protected List<String> getParameters() {
        return List.of(String.valueOf(pinNumber), String.valueOf(durationInSeconds));
    }

    public static EnablePinForDuration fromDto(EnablePinForDurationRequestDTO dto) {
        return new EnablePinForDuration(Esp32Devkit1Pin.fromPinNumber(dto.getPinNumber()),
                Duration.ofSeconds(dto.getDurationInSeconds()));
    }
}
