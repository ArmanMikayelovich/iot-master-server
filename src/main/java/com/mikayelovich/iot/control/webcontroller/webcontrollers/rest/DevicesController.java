package com.mikayelovich.iot.control.webcontroller.webcontrollers.rest;

import com.mikayelovich.iot.control.webcontroller.connectors.SocketBasedStringCommandPublisher;
import com.mikayelovich.iot.control.webcontroller.listener.SocketConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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



}
