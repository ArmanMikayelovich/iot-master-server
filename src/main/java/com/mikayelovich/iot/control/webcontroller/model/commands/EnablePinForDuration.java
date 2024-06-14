package com.mikayelovich.iot.control.webcontroller.model.commands;

import com.mikayelovich.iot.control.webcontroller.model.dto.rest.EnablePinForDurationRequestDTO;
import com.mikayelovich.iot.control.webcontroller.model.microcontrontroller.esp32.Esp32Devkit1Pin;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EnablePinForDuration extends DigitalWriteCommand {
    private final static String ENABLE_PIN_FOR = "enablePinFor";

    private final long durationInSeconds;


    public EnablePinForDuration(Esp32Devkit1Pin pinNumber, Duration duration) {
        super(pinNumber, DigitalWriteState.HIGH);
        this.durationInSeconds = duration.toSeconds();
    }

    @Override
    protected String getCommandName() {
        return ENABLE_PIN_FOR;
    }

    @Override
    protected List<String> getParameters() {
        ArrayList<String> params = new ArrayList<>(super.getParameters());
        params.add(String.valueOf(durationInSeconds));
        return params;
    }

    public static EnablePinForDuration fromDto(EnablePinForDurationRequestDTO dto) {
        return new EnablePinForDuration(Esp32Devkit1Pin.fromPinNumber(dto.getPinNumber()),
                Duration.ofSeconds(dto.getDurationInSeconds()));
    }
}
