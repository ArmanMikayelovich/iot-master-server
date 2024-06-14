package com.mikayelovich.iot.control.webcontroller.connectors;

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
public class CommandPublisherExecutorService {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final SocketConnectionPool socketConnectionPool;

    /**
     * Constructs a new CommandPublisherExecutorService with the specified SocketConnectionPool.
     *
     * @param socketConnectionPool the SocketConnectionPool to use for retrieving SocketBasedStringCommandPublishers
     */
    @Autowired
    public CommandPublisherExecutorService(SocketConnectionPool socketConnectionPool) {
        this.socketConnectionPool = socketConnectionPool;
    }

    /**
     * Executes a command specified in the RequestDTO on a separate thread.
     *
     * @param requestDTO the RequestDTO containing the command details
     */
    public void executeCommand(RequestDTO requestDTO) {
        Runnable run = () -> {
            Optional<SocketBasedStringCommandPublisher> commandPublisherOptional = socketConnectionPool.getByMacId(requestDTO.getMacAddress());
            if (commandPublisherOptional.isPresent()) {
                StringExecutableCommand stringExecutableCommand = StringExecutableCommand.fromDto(requestDTO);
                SocketBasedStringCommandPublisher socketBasedStringCommandPublisher = commandPublisherOptional.get();
                socketBasedStringCommandPublisher.sendCommand(stringExecutableCommand);
                log.debug("Command '{}' sent to device with MAC address: {}", stringExecutableCommand.mapToExecutable(), requestDTO.getMacAddress());
            } else {
                log.warn("No connection found for device with MAC address: {}", requestDTO.getMacAddress());
            }
        };
        executorService.execute(run);
        log.debug("Command execution scheduled for device with MAC address: {}", requestDTO.getMacAddress());
    }
}