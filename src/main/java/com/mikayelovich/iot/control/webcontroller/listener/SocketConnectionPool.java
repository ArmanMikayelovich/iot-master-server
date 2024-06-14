package com.mikayelovich.iot.control.webcontroller.listener;

import com.mikayelovich.iot.control.webcontroller.connectors.SocketBasedStringCommandPublisher;
import lombok.extern.slf4j.Slf4j;
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

    public List<SocketBasedStringCommandPublisher> getAllActiveConnections() {
        List<SocketBasedStringCommandPublisher> activeConnections = map.values().stream()
                .filter(SocketBasedStringCommandPublisher::isActive)
                .collect(Collectors.toList());
        log.debug("Retrieved all active connections");
        return activeConnections;
    }

    public String addConnection(Socket socket) {
        SocketBasedStringCommandPublisher publisher = new SocketBasedStringCommandPublisher(socket);
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
    //TODO link pseudonym to mac address, for easier recognition
}
