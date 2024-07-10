package com.mikayelovich.iot.control.model.commands.abstraction;

import com.mikayelovich.iot.control.model.commands.DigitalWriteCommand;
import com.mikayelovich.iot.control.model.commands.EnablePinForDuration;
import com.mikayelovich.iot.control.model.commands.ReadCommand;
import com.mikayelovich.iot.control.model.commands.RestartCommand;
import com.mikayelovich.iot.control.model.commands.PinModeCommand;
import com.mikayelovich.iot.control.model.dto.rest.DigitalWriteCommandRequestDTO;
import com.mikayelovich.iot.control.model.dto.rest.EnablePinForDurationRequestDTO;
import com.mikayelovich.iot.control.model.dto.rest.PinModeCommandRequestDto;
import com.mikayelovich.iot.control.model.dto.rest.ReadCommandRequestDTO;
import com.mikayelovich.iot.control.model.dto.rest.RequestDTO;
import com.mikayelovich.iot.control.model.dto.rest.RestartCommandRequestDto;

import java.util.List;

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

    //todo move to separate mapper
    public static StringExecutableCommand fromDto(RequestDTO dto) {
        if (dto instanceof DigitalWriteCommandRequestDTO) {
            return DigitalWriteCommand.fromDto((DigitalWriteCommandRequestDTO) dto);
        } else if (dto instanceof ReadCommandRequestDTO) {
            return ReadCommand.fromDto((ReadCommandRequestDTO) dto);
        } else if (dto instanceof EnablePinForDurationRequestDTO) {
            return EnablePinForDuration.fromDto((EnablePinForDurationRequestDTO) dto);
        } else if (dto instanceof PinModeCommandRequestDto) {
            return PinModeCommand.fromDto((PinModeCommandRequestDto) dto);
        } else if (dto instanceof RestartCommandRequestDto) {
            return RestartCommand.fromDto((RestartCommandRequestDto) dto);
        } else {
            throw new IllegalArgumentException("no appropriate class for DTO to map");
        }
    }
}
