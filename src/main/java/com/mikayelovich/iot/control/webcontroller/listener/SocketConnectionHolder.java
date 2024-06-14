package com.mikayelovich.iot.control.webcontroller.listener;

import com.mikayelovich.iot.control.webcontroller.connectors.SocketBasedStringCommandPublisher;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SocketConnectionHolder {
    private final Map<UUID, SocketBasedStringCommandPublisher> map = new HashMap<>();

    public List<SocketBasedStringCommandPublisher> getAllActiveConnections() {
        return map.values().stream()
                .filter(SocketBasedStringCommandPublisher::isActive)
                .collect(Collectors.toList());
    }


    public UUID addConnection(Socket socket) {
        SocketBasedStringCommandPublisher publisher = new SocketBasedStringCommandPublisher(socket);
        UUID deviceUniqueId = publisher.getDeviceUniqueId();
        map.put(deviceUniqueId, publisher);
        return deviceUniqueId;
    }

    public Optional<SocketBasedStringCommandPublisher> getById(UUID uuid) {
        return Optional.ofNullable(map.get(uuid));
    }
}
