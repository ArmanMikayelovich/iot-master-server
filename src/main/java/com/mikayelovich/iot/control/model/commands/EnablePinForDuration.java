package com.mikayelovich.iot.control.model.commands;

import com.mikayelovich.iot.control.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.model.dto.rest.EnablePinForDurationRequestDTO;

import java.time.Duration;
import java.util.List;

public class EnablePinForDuration extends StringExecutableCommand {
    private final static String ENABLE_PIN_FOR = "enablePinFor";

    private final Integer pin;

    private final long durationInSeconds;


    public EnablePinForDuration(Integer pin, Duration duration) {
        this.pin = pin;
        this.durationInSeconds = duration.toSeconds();
    }

    @Override
    protected String getCommandName() {
        return ENABLE_PIN_FOR;
    }

    @Override
    protected List<String> getParameters() {
        return List.of(String.valueOf(pin), String.valueOf(durationInSeconds));
    }

    public static EnablePinForDuration fromDto(EnablePinForDurationRequestDTO dto) {
        return new EnablePinForDuration(dto.getPinNumber(),
                Duration.ofSeconds(dto.getDurationInSeconds()));
    }
}
