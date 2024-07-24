package com.mikayelovich.iot.control.mqttcontroler.events;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MqttReceivedEvent  extends MqttEvent {
    private final String publishedEventId;

    public MqttReceivedEvent(String publishedEventId, String data) {
        setData(data);
        this.publishedEventId = publishedEventId;
    }
    @Override
    public String toString() {
        return "MqttReceivedEvent{" +
                "publishedEventId='" + publishedEventId + '\'' +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                '}';
    }
}
