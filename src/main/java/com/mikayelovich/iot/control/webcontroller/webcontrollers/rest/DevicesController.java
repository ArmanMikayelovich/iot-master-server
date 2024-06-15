package com.mikayelovich.iot.control.webcontroller.webcontrollers.rest;

import com.mikayelovich.iot.control.webcontroller.connectors.SocketBasedStringCommandPublisher;
import com.mikayelovich.iot.control.webcontroller.listener.SocketConnectionPool;
import com.mikayelovich.iot.control.webcontroller.model.commands.enums.PinModeState;
import com.mikayelovich.iot.control.webcontroller.model.microcontrontroller.esp32.Esp32Devkit1Pin;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/devices")
public class DevicesController {

    private final SocketConnectionPool socketConnectionPool;

    @Autowired
    public DevicesController(SocketConnectionPool socketConnectionPool) {
        this.socketConnectionPool = socketConnectionPool;
    }


    @GetMapping
    public List<String> getAll() {
        return socketConnectionPool.getAllActiveConnections().stream()
                .map(SocketBasedStringCommandPublisher::getDeviceMacAddress)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/pin-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<Pair<Esp32Devkit1Pin, PinModeState>>> getPinInfo() {
        return socketConnectionPool.getPinsInfo();
    }
}
