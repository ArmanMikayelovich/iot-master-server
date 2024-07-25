package com.mikayelovich.iot.control.model.commands;

import com.mikayelovich.iot.control.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.model.dto.rest.ReadCommandRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ReadCommand extends StringExecutableCommand {
    private final static String DIGITAL_READ = "digitalRead";

    private final Integer pin;


    @Override
    protected String getCommandName() {
        return DIGITAL_READ;
    }

    @Override
    protected List<String> getParameters() {
        return List.of(String.valueOf(pin));
    }


    public static ReadCommand fromDto(ReadCommandRequestDTO dto) {
        return new ReadCommand(dto.getPinNumber());
    }
}
