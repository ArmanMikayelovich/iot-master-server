package com.mikayelovich.iot.control.webcontroller.webcontrollers.rest;

import com.mikayelovich.iot.control.webcontroller.connectors.CommandPublisherExecutorService;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.DigitalWriteCommandRequestDTO;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.EnablePinForDurationRequestDTO;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.PinModeCommandRequestDto;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.RestartCommandRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/execute")
public class CommandExecutionController {

    private final CommandPublisherExecutorService commandPublisherExecutorService;

    @Autowired
    public CommandExecutionController(CommandPublisherExecutorService commandPublisherExecutorService) {
        this.commandPublisherExecutorService = commandPublisherExecutorService;
    }

    @PostMapping(value = "/digital-write/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody DigitalWriteCommandRequestDTO dto) {
        commandPublisherExecutorService.executeCommand(dto);
    }

    @PostMapping(value = "/enable-pin-for-duration/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody EnablePinForDurationRequestDTO dto) {
        commandPublisherExecutorService.executeCommand(dto);
    }

    @PostMapping(value = "/pin-mode/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody PinModeCommandRequestDto dto) {
        commandPublisherExecutorService.executeCommand(dto);
    }

    @PostMapping(value = "/restart-device/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void restart(@Valid @RequestBody RestartCommandRequestDto dto) {
        commandPublisherExecutorService.executeCommand(dto);
    }

    //todo read command...
}
