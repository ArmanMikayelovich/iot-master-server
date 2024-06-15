package com.mikayelovich.iot.control.webcontroller.model.commands;

import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.webcontroller.model.commands.enums.PinModeState;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.EnablePinForDurationRequestDTO;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.PinModeCommandRequestDto;
import com.mikayelovich.iot.control.webcontroller.model.microcontrontroller.esp32.Esp32Devkit1Pin;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor
public class PinModeCommand extends StringExecutableCommand {

    private final static String PIN_MODE = "pinMode";

    private Esp32Devkit1Pin pin;
    private PinModeState pinModeState;

    @Override
    protected String getCommandName() {
        return PIN_MODE;
    }

    @Override
    protected List<String> getParameters() {
        return List.of(String.valueOf(pin), String.valueOf(pinModeState));
    }


    public static PinModeCommand fromDto(PinModeCommandRequestDto dto) {
        return new PinModeCommand(Esp32Devkit1Pin.fromPinNumber(dto.getPinNumber()), PinModeState.valueOf(dto.getPinModeState()));
    }

}
