package com.mikayelovich.iot.control.webcontroller.connectors;

import com.mikayelovich.iot.control.webcontroller.exceptions.SocketClosedException;
import com.mikayelovich.iot.control.webcontroller.exceptions.SocketStreamInitializationException;
import com.mikayelovich.iot.control.webcontroller.model.commands.PinModeCommand;
import com.mikayelovich.iot.control.webcontroller.model.commands.RestartCommand;
import com.mikayelovich.iot.control.webcontroller.model.commands.abstraction.Command;
import com.mikayelovich.iot.control.webcontroller.util.InetUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class SocketBasedStringCommandPublisher implements CommandPublisher<String> {

    private @Getter final String deviceMacAddress;
    private final Socket socket;
    private final PrintWriter printToSocketWriter;
    private @Getter boolean isActive = true;
    private final DevicePinStateHolder devicePinStateHolder;

    public SocketBasedStringCommandPublisher(Socket socket, DevicePinStateHolder devicePinStateHolder) {
        this.socket = socket;
        this.devicePinStateHolder = devicePinStateHolder;
        try {
            printToSocketWriter = new PrintWriter(socket.getOutputStream(), true);
            this.deviceMacAddress = InetUtil.getMacAddress(socket.getInetAddress());
            log.info("SocketBasedStringCommandPublisher initialized for device with MAC address: {}", deviceMacAddress);
        } catch (IOException exception) {
            log.error("Error initializing SocketBasedStringCommandPublisher", exception);
            throw new SocketStreamInitializationException(exception.getMessage());
        }
    }

    public void sendCommand(Command<String> command) {
        if (!isActive) {
            throw new SocketClosedException();
        }
        String commandStr = command.mapToExecutable();
        log.debug("Sending command '{}' to device with MAC address: {}", commandStr, deviceMacAddress);
        printToSocketWriter.println(commandStr);
        if (command instanceof PinModeCommand) {
            devicePinStateHolder.put(deviceMacAddress, ((PinModeCommand) command));
        } else if (command instanceof RestartCommand) {
            devicePinStateHolder.removeAllForDevice(deviceMacAddress);
        }
    }

    @Override
    public void close() {
        try {
            printToSocketWriter.close();
            socket.close();
            isActive = false;
            log.info("SocketBasedStringCommandPublisher closed for device with MAC address: {}", deviceMacAddress);
        } catch (IOException e) {
            log.error("Error closing SocketBasedStringCommandPublisher", e);
        }
    }
}