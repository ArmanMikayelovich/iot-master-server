package com.mikayelovich.iot.control.webcontroller.listener;

import com.mikayelovich.iot.control.webcontroller.connectors.DevicePinStateHolder;
import com.mikayelovich.iot.control.webcontroller.connectors.SocketBasedStringCommandPublisher;
import com.mikayelovich.iot.control.webcontroller.model.commands.enums.PinModeState;
import com.mikayelovich.iot.control.webcontroller.model.microcontrontroller.esp32.Esp32Devkit1Pin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SocketConnectionPool {
    private final Map<String, SocketBasedStringCommandPublisher> map = new HashMap<>();
    private final DevicePinStateHolder devicePinStateHolder;

    @Autowired
    public SocketConnectionPool( ) {
        this.devicePinStateHolder = new DevicePinStateHolder();
    }


    public String addConnection(Socket socket) {
        SocketBasedStringCommandPublisher publisher = new SocketBasedStringCommandPublisher(socket,devicePinStateHolder);
        String deviceUniqueId = publisher.getDeviceMacAddress();
        map.put(deviceUniqueId, publisher);
        log.info("Added new connection with device unique ID: {}", deviceUniqueId);
        return deviceUniqueId;
    }

    public Optional<SocketBasedStringCommandPublisher> getByMacId(String id) {
        SocketBasedStringCommandPublisher publisher = map.get(id);
        if (publisher != null) {
            log.debug("Retrieved connection for device unique ID: {}", id);
        } else {
            log.warn("No connection found for device unique ID: {}", id);
        }
        return Optional.ofNullable(publisher);
    }

    public List<SocketBasedStringCommandPublisher> getAllActiveConnections() {
        List<SocketBasedStringCommandPublisher> activeConnections = map.values().stream()
                .filter(SocketBasedStringCommandPublisher::isActive)
                .collect(Collectors.toList());
        log.debug("Retrieved all active connections");
        return activeConnections;
    }


    public Map<String, List<Pair<Esp32Devkit1Pin, PinModeState>>> getPinsInfo() {
        return devicePinStateHolder.getAllDevicePinStates();
    }

    //TODO link pseudonym to mac address, for easier recognition
}
