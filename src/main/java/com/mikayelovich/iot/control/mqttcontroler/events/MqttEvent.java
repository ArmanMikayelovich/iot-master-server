package com.mikayelovich.iot.control.mqttcontroler.events;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class MqttEvent {
    protected final LocalDateTime timestamp = LocalDateTime.now();
    protected String data;
}
