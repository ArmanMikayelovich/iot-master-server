package com.mikayelovich.iot.control.mqttcontroler.listener;

import com.mikayelovich.iot.control.mqttcontroler.StateMachine;
import com.mikayelovich.iot.control.mqttcontroler.events.MqttReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MqttMessageHandlerService {

    @Autowired
    private StateMachine stateMachine;

    @Value("${mqtt.event-listener-topic.message-format-regex}")
    private String regexPattern;
    // Compile the regular expression
    private Pattern pattern;

    @PostConstruct
    public void init() {
        pattern = Pattern.compile(regexPattern);
    }
    private String lastHandledMessage;

    public void handleMessage(String topic, String payload) {
        System.out.println("Handling message from topic: " + topic + " with payload: " + payload);
        lastHandledMessage = payload;
        Matcher matcher = pattern.matcher(payload);
        if (matcher.find()) {
            String eventTopic = matcher.group(1);
            String publishedEventId = matcher.group(2);
            String answerData = matcher.group(3);

            System.out.println("Topic: " + topic);
            System.out.println("Published Event ID: " + publishedEventId);
            System.out.println("Answer Data: " + answerData);

            addMqttEventIntoStateMachine(eventTopic, publishedEventId, answerData);
        } else {
            System.out.println("No match found.");
        }
    }

    private void addMqttEventIntoStateMachine(String eventTopic, String publishedEventId, String answerData) {
        MqttReceivedEvent mqttReceivedEvent = new MqttReceivedEvent(publishedEventId, answerData);
        stateMachine.registerEvent(eventTopic, mqttReceivedEvent);
    }

    public String getLastHandledMessage() {
        return lastHandledMessage;
    }
}