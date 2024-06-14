package com.mikayelovich.iot.control.webcontroller.rest;

import com.mikayelovich.iot.control.webcontroller.connectors.CommandPublisherExecutorService;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.DigitalWriteCommandRequestDTO;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.EnablePinForDurationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/execute")
public class CommandExecutionController {

    private final CommandPublisherExecutorService commandPublisherExecutorService;

    @Autowired
    public CommandExecutionController(CommandPublisherExecutorService commandPublisherExecutorService) {
        this.commandPublisherExecutorService = commandPublisherExecutorService;
    }

    @PostMapping("/digital-write/")
    public void digitalWrite(DigitalWriteCommandRequestDTO dto) {
        commandPublisherExecutorService.executeCommand(dto);
    }

    @PostMapping("/enable-pin-for-duration/")
    public void digitalWrite(EnablePinForDurationRequestDTO dto) {
        commandPublisherExecutorService.executeCommand(dto);
    }

    //todo read command...
}
