package com.mikayelovich.iot.control.webcontroller.model.commands;

import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.RestartCommandRequestDto;

import java.util.List;

public class RestartCommand extends StringExecutableCommand {
    private static final String RESTART = "restart";

    @Override
    protected String getCommandName() {
        return RESTART;
    }

    @Override
    protected List<String> getParameters() {
        return List.of();
    }

    //todo refactor
    public static RestartCommand fromDto(RestartCommandRequestDto dto) {
        return new RestartCommand();
    }
}
