package com.mikayelovich.iot.control.mqttcontroler.events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MqttPublishedEvent  extends MqttEvent{

    private final String uniqueId;

    public MqttPublishedEvent(String topicId, String data) {
        this.data = data;
        uniqueId = IdGenerator.generateId(topicId);
    }

    private final static class IdGenerator {
        private static final Map<String, Integer> topicNamesWithIdIncrementor = new HashMap<>();

        private static String generateId(String topicId) {
            topicNamesWithIdIncrementor.putIfAbsent(topicId, 1);
            return "topicId_" + topicNamesWithIdIncrementor.put(topicId, topicNamesWithIdIncrementor.get(topicId) + 1);
        }
    }

    @Override
    public String toString() {
        return "MqttPublishedEvent{" +
                "uniqueId='" + uniqueId + '\'' +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                '}';
    }
}
