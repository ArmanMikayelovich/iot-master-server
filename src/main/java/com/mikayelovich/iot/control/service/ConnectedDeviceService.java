package com.mikayelovich.iot.control.service;

import com.mikayelovich.iot.control.mqttcontroler.StateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectedDeviceService {

    @Autowired
    private StateMachine stateMachine;
    public List<String> getCurrentConnectedDevices() {
        return stateMachine.getNames();
    }

}
