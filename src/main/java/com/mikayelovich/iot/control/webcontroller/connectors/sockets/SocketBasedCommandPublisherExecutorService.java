package com.mikayelovich.iot.control.webcontroller.connectors.sockets;

import com.mikayelovich.iot.control.webcontroller.listener.SocketConnectionPool;
import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.StringExecutableCommand;
import com.mikayelovich.iot.control.webcontroller.model.dto.rest.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class SocketBasedCommandPublisherExecutorService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private final SocketConnectionPool socketConnectionPool;

    /**
     * Constructs a new SocketBasedCommandPublisherExecutorService with the specified SocketConnectionPool.
     *
     * @param socketConnectionPool the SocketConnectionPool to use for retrieving SocketBasedStringCommandPublishers
     */
    @Autowired
    public SocketBasedCommandPublisherExecutorService(SocketConnectionPool socketConnectionPool) {
        this.socketConnectionPool = socketConnectionPool;
    }

    /**
     * Executes a command specified in the RequestDTO on a separate thread.
     *
     * @param requestDTO the RequestDTO containing the command details
     */
    public void executeCommand(RequestDTO requestDTO) {
        Runnable run = () -> {
            Optional<SocketBasedStringCommandPublisher> commandPublisherOptional = socketConnectionPool.getByMacId(requestDTO.getUniqueDeviceId());
            if (commandPublisherOptional.isPresent()) {
                StringExecutableCommand stringExecutableCommand = StringExecutableCommand.fromDto(requestDTO);
                SocketBasedStringCommandPublisher socketBasedStringCommandPublisher = commandPublisherOptional.get();
                socketBasedStringCommandPublisher.sendCommand(stringExecutableCommand);
                log.debug("Command '{}' sent to device with MAC address: {}", stringExecutableCommand.mapToExecutable(), requestDTO.getUniqueDeviceId());
            } else {
                log.warn("No connection found for device with MAC address: {}", requestDTO.getUniqueDeviceId());
            }
        };
        executorService.execute(run);
        log.debug("Command execution scheduled for device with MAC address: {}", requestDTO.getUniqueDeviceId());
    }
}