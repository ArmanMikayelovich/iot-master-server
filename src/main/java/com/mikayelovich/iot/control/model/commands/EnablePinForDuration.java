package com.mikayelovich.iot.control.model.commands;

import com.mikayelovich.iot.control.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.model.dto.rest.EnablePinForDurationRequestDTO;
import com.mikayelovich.iot.control.model.microcontrontroller.esp32.Esp32Devkit1Pin;

import java.time.Duration;
import java.util.List;

public class EnablePinForDuration extends StringExecutableCommand {
    private final static String ENABLE_PIN_FOR = "enablePinFor";

    private final Integer pinNumber;

    private final long durationInSeconds;


    public EnablePinForDuration(Integer pinNumber, Duration duration) {
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
        return new EnablePinForDuration(dto.getPinNumber(),
                Duration.ofSeconds(dto.getDurationInSeconds()));
    }
}
