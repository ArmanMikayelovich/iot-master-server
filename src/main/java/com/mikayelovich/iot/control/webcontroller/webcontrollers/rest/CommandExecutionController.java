package com.mikayelovich.iot.control.webcontroller.webcontrollers.rest;

import com.mikayelovich.iot.control.webcontroller.connectors.sockets.SocketBasedCommandPublisherExecutorService;
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

    private final SocketBasedCommandPublisherExecutorService socketBasedCommandPublisherExecutorService;

    @Autowired
    public CommandExecutionController(SocketBasedCommandPublisherExecutorService socketBasedCommandPublisherExecutorService) {
        this.socketBasedCommandPublisherExecutorService = socketBasedCommandPublisherExecutorService;
    }

    @PostMapping(value = "/digital-write/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody DigitalWriteCommandRequestDTO dto) {
        socketBasedCommandPublisherExecutorService.executeCommand(dto);
    }

    @PostMapping(value = "/enable-pin-for-duration/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody EnablePinForDurationRequestDTO dto) {
        socketBasedCommandPublisherExecutorService.executeCommand(dto);
    }

    @PostMapping(value = "/pin-mode/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody PinModeCommandRequestDto dto) {
        socketBasedCommandPublisherExecutorService.executeCommand(dto);
    }

    @PostMapping(value = "/restart-device/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void restart(@Valid @RequestBody RestartCommandRequestDto dto) {
        socketBasedCommandPublisherExecutorService.executeCommand(dto);
    }

    //todo read command...
}
