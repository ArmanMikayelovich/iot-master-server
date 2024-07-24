package com.mikayelovich.iot.control.mqttcontroler;

import com.mikayelovich.iot.control.mqttcontroler.events.MqttEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StateMachine {

    private final Map<String, List<MqttEvent>> topicsWithEvents = new HashMap<>();


    public void registerEvent(String topic, MqttEvent event) {
        topicsWithEvents.putIfAbsent(topic, new ArrayList<>());
        topicsWithEvents.get(topic).add(event);
    }

    public String showData() {
        return topicsWithEvents.toString();
    }


    public List<String> getNames() {
        return new ArrayList<>(topicsWithEvents.keySet());
    }
}
