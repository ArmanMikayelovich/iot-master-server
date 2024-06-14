package com.mikayelovich.iot.control.webcontroller.model.commands;

import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.DigitalWriteCommandRequestDTO;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.ReadCommandRequestDTO;
import com.mikayelovich.iot.control.webcontroller.model.microcontrontroller.esp32.Esp32Devkit1Pin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ReadCommand extends StringExecutableCommand {
    private final static String DIGITAL_READ = "digitalRead";

    private final Esp32Devkit1Pin pinNumber;


    @Override
    protected String getCommandName() {
        return DIGITAL_READ;
    }

    @Override
    protected List<String> getParameters() {
        return List.of(String.valueOf(pinNumber));
    }


    public static ReadCommand fromDto(ReadCommandRequestDTO dto) {
        return new ReadCommand(Esp32Devkit1Pin.fromPinNumber(dto.getPinNumber()));
    }
}
