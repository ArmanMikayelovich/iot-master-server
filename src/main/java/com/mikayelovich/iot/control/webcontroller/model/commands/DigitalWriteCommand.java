package com.mikayelovich.iot.control.webcontroller.model.commands;

import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.StringExecutableCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DigitalWriteCommand extends StringExecutableCommand {
    private final static String DIGITAL_WRITE = "digitalWrite";

    private final int pinNumber;
    private final DigitalWriteState state;

    @Override
    protected String getCommandName() {
        return DIGITAL_WRITE;
    }

    @Override
    protected List<String> getParameters() {
        return List.of(String.valueOf(pinNumber), String.valueOf(state));
    }

}
