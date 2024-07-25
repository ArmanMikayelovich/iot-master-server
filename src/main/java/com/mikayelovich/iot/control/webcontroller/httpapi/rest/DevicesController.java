package com.mikayelovich.iot.control.webcontroller.httpapi.rest;

import com.mikayelovich.iot.control.model.commands.enums.PinModeState;
import com.mikayelovich.iot.control.service.ConnectedDeviceService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devices")
public class DevicesController {

    private final ConnectedDeviceService connectedDeviceService;

    @Autowired
    public DevicesController(ConnectedDeviceService connectedDeviceService) {
        this.connectedDeviceService = connectedDeviceService;
    }


    @GetMapping
    public List<String> getAll() {
        return connectedDeviceService.getCurrentConnectedDevices();
    }

    @GetMapping(value = "/pin-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<Pair<Integer, PinModeState>>> getPinInfo(@RequestParam(required = false) String uniqueId) {
    //TODO create a new command for esp systems, to check their state of pins, and send into back as
        return new HashMap<>();
    }

}
