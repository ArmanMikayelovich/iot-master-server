package com.mikayelovich.iot.control.webcontroller.listener;

import com.mikayelovich.iot.control.webcontroller.exceptions.ServerSockerInitException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class SocketConnectionListener {

    private final SocketConnectionPool socketConnectionPool;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private ServerSocket serverSocket;
    @Value("${server.socketlistener.port}")
    private int port;

    @Autowired
    public SocketConnectionListener(SocketConnectionPool socketConnectionPool) {
        this.socketConnectionPool = socketConnectionPool;
    }

    @PostConstruct
    public void init() {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Socket connection listener initialized");
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> {
                    try {
                        String s = socketConnectionPool.addConnection(socket);
                        log.info("connection successfully added, MAC address: {}", s);
                    } catch (Exception e) {
                        log.error("Error handling incoming connection", e);
                    }
                });
            }
        } catch (IOException exception) {
            log.error("Error initializing server socket", exception);
            throw new ServerSockerInitException(exception.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            serverSocket.close();
            log.info("Socket connection listener stopped");
        } catch (IOException e) {
            log.error("Error shutting down server socket", e);
        }
    }
}