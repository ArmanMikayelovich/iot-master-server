package com.mikayelovich.iot.control.webcontroller.httpapi.rest;

import com.mikayelovich.iot.control.model.commands.DigitalWriteCommand;
import com.mikayelovich.iot.control.model.commands.EnablePinForDuration;
import com.mikayelovich.iot.control.model.commands.PinModeCommand;
import com.mikayelovich.iot.control.model.commands.RestartCommand;
import com.mikayelovich.iot.control.mqttcontroler.publisher.MQTTMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/execute")
public class MqttCommandExecutionController {

    private final MQTTMessagePublisher mqttMessagePublisher;

    @Autowired
    public MqttCommandExecutionController(MQTTMessagePublisher mqttMessagePublisher) {
        this.mqttMessagePublisher = mqttMessagePublisher;
    }

    @PostMapping(value = "/digital-write/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody DigitalWriteCommand dto) {
        mqttMessagePublisher.sendCommand(dto);
    }

    @PostMapping(value = "/enable-pin-for-duration/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody EnablePinForDuration dto) {
        mqttMessagePublisher.sendCommand(dto);
    }

    @PostMapping(value = "/pin-mode/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void digitalWrite(@Valid @RequestBody PinModeCommand dto) {
        mqttMessagePublisher.sendCommand(dto);
    }

    @PostMapping(value = "/restart-device/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void restart(@Valid @RequestBody RestartCommand dto) {
        mqttMessagePublisher.sendCommand(dto);
    }


}
