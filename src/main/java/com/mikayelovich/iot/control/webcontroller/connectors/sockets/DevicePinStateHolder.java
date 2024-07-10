package com.mikayelovich.iot.control.webcontroller.connectors.sockets;

import com.mikayelovich.iot.control.model.commands.PinModeCommand;
import com.mikayelovich.iot.control.model.commands.enums.PinModeState;
import com.mikayelovich.iot.control.model.microcontrontroller.esp32.Esp32Devkit1Pin;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DevicePinStateHolder {
    private final HashMap<Pair<String, Integer>, PinModeState> map = new HashMap<>();

    public void put(String macAddress, PinModeCommand command) {
        Integer pin = command.getPin();
        PinModeState pinModeState = command.getPinModeState();
        Pair<String, Integer> pair = Pair.of(macAddress, pin);
        map.put(pair, pinModeState);
    }

    public void removeAllForDevice(String macAddress) {
                map.keySet().stream()
                        .filter(key -> key.getLeft().equals(macAddress))
                        .collect(Collectors.toList()).forEach(map::remove);
    }


    public Map<String, List<Pair<Integer, PinModeState>>> getAllDevicePinStates() {
        HashMap<String, List<Pair<Integer, PinModeState>>> macAddressMap = new HashMap<>();

        for (Map.Entry<Pair<String, Integer>, PinModeState> entry : map.entrySet()) {
            Pair<String, Integer> key = entry.getKey();
            PinModeState state = entry.getValue();

            String macAddress = key.getKey();
            Integer pin = key.getValue();

            // If macAddress key doesn't exist in macAddressMap, create a new list
            macAddressMap.putIfAbsent(macAddress, new ArrayList<>());
            List<Pair<Integer, PinModeState>> pinStateList = macAddressMap.get(macAddress);

            // Add the pin and state pair to the list
            pinStateList.add(Pair.of(pin, state));
        }
        return macAddressMap;
    }
}
