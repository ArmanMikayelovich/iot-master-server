package com.mikayelovich.iot.control.model.commands;

import com.mikayelovich.iot.control.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.model.commands.enums.PinModeState;
import com.mikayelovich.iot.control.model.dto.rest.PinModeCommandRequestDto;
import com.mikayelovich.iot.control.model.microcontrontroller.esp32.Esp32Devkit1Pin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PinModeCommand extends StringExecutableCommand {

    private final static String PIN_MODE = "pinMode";

    private Integer pin;
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
        return new PinModeCommand(dto.getPinNumber(), PinModeState.valueOf(dto.getPinModeState()));
    }

}
