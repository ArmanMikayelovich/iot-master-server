package com.mikayelovich.iot.control.webcontroller.model.commands.abstraction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class StringExecutableCommand implements Command<String> {

    public final String toString() {
        String methodParams = String.join(",", getParameters());
        return String.format("%s(%s)", getCommandName(), methodParams);
    }

    @Override
    public final String mapToExecutable() {
        return toString();
    }

    protected abstract String getCommandName();

    protected abstract List<String> getParameters();
}
