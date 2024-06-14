package com.mikayelovich.iot.control.webcontroller.connectors;

import com.mikayelovich.iot.control.webcontroller.exceptions.SocketClosedException;
import com.mikayelovich.iot.control.webcontroller.exceptions.SocketStreamInitializationException;
import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.Command;
import com.mikayelovich.iot.control.webcontroller.util.InetUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
public class SocketBasedStringCommandPublisher implements CommandPublisher<String> {

    private @Getter final String deviceMacAddress;
    private final Socket socket;
    private final PrintWriter printToSocketWriter;
    private @Getter boolean isActive = true;

    public SocketBasedStringCommandPublisher(Socket socket) {
        this.socket = socket;
        try {
            printToSocketWriter = new PrintWriter(socket.getOutputStream(), true);
            this.deviceMacAddress = InetUtil.getMacAddress(socket.getInetAddress());
        } catch (IOException exception) {
            log.error(exception.getMessage(), Arrays.toString(exception.getStackTrace()));
            throw new SocketStreamInitializationException(exception.getMessage());
        }
    }


    public void sendCommand(Command<String> command) {
        if (!isActive) {
            throw new SocketClosedException();
        }
        String commandStr = command.mapToExecutable();
        printToSocketWriter.println(commandStr);
    }


    @Override
    public void close() {
        try {
            printToSocketWriter.close();
            socket.close();
            isActive = true;
        } catch (IOException e) {
            log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }
}
