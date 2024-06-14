package com.mikayelovich.iot.control.webcontroller.listener;

import com.mikayelovich.iot.control.webcontroller.exceptions.ServerSockerInitException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

@Component
@Slf4j
public class SocketConnectionListener {


    private final SocketConnectionHolder socketConnectionHolder;

    private ServerSocket serverSocket;
    @Value("${server.socketlistener.port}")
    private int port;

    @Autowired
    public SocketConnectionListener(SocketConnectionHolder socketConnectionHolder) {
        this.socketConnectionHolder = socketConnectionHolder;
    }

    @PostConstruct
    public void init() {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Socket connection listener initialized");
            while (true) {
                Socket socket = serverSocket.accept();
                socketConnectionHolder.addConnection(socket);
            }
        } catch (IOException exception) {
            log.error(exception.getMessage(), Arrays.toString(exception.getStackTrace()));
            throw new ServerSockerInitException(exception.getMessage());
        }

    }
}
