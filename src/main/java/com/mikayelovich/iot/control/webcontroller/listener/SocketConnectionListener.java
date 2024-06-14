package com.mikayelovich.iot.control.webcontroller.listener;

import com.mikayelovich.iot.control.webcontroller.exceptions.ServerSockerInitException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

@Component
@Slf4j
public class SocketConnectionListener {

    private ServerSocket serverSocket;

    @Value("${server.socketlistener.port}")
    private int port;

    @PostConstruct
    public void init() {
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException exception) {
            log.error(exception.getMessage(), Arrays.toString(exception.getStackTrace()));
            throw new ServerSockerInitException(exception.getMessage());
        }

        log.info("Socket connection listener initialized");
    }
}
