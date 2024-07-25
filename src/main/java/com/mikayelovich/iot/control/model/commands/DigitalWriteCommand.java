package com.mikayelovich.iot.control.model.commands;

import com.mikayelovich.iot.control.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.model.commands.enums.DigitalWriteState;
import com.mikayelovich.iot.control.model.dto.rest.DigitalWriteCommandRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DigitalWriteCommand extends StringExecutableCommand {
    private final static String DIGITAL_WRITE = "digitalWrite";

    private final Integer pin;
    private final DigitalWriteState state;

    @Override
    protected String getCommandName() {
        return DIGITAL_WRITE;
    }

    @Override
    protected List<String> getParameters() {
        return List.of(String.valueOf(pin), String.valueOf(state));
    }


    public static DigitalWriteCommand fromDto(DigitalWriteCommandRequestDTO dto) {
        DigitalWriteState digitalWriteState = DigitalWriteState.valueOf(dto.getState());
        return new DigitalWriteCommand(dto.getPinNumber(), digitalWriteState);
    }
}
