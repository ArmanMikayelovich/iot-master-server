package com.mikayelovich.iot.control.webcontroller.model.commands;

import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.StringExecutableCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ReadCommand extends StringExecutableCommand {
    private final static String DIGITAL_READ = "digitalRead";

    private final int pinNumber;


    @Override
    protected String getCommandName() {
        return DIGITAL_READ;
    }

    @Override
    protected List<String> getParameters() {
        return List.of(String.valueOf(pinNumber));
    }

}
