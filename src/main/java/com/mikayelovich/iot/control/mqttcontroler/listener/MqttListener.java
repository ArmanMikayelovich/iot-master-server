package com.mikayelovich.iot.control.mqttcontroler.listener;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MqttListener {

    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final int BASE_RECONNECT_DELAY_MS = 1000;

    @Value("${mqtt.broker.host}")
    private String brokerHost;

    @Value("${mqtt.broker.port}")
    private String brokerPort;

    @Value("${mqtt.broker.username}")
    private String username;

    @Value("${mqtt.broker.password}")
    private String password;

    @Value("${mqtt.registration-topic}")
    private String registrationTopic;

    @Autowired
    private MqttMessageHandlerService mqttMessageHandlerService;

    private MqttClient client;


    @PostConstruct
    public void init() {
        try {
            String brokerUri = "tcp://" + brokerHost + ":" + brokerPort;
            client = new MqttClient(brokerUri, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setCleanSession(true);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("Connection lost: {}", cause.getMessage());
                    handleReconnection(options);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String payload = new String(message.getPayload());
                    log.info("Received message: {} from topic: {}", payload, topic);
                    mqttMessageHandlerService.handleMessage(topic, payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.debug("Delivery complete for token: {}", token.getMessageId());
                }
            });

            client.connect(options);
            client.subscribe(registrationTopic);
            log.info("Connected to MQTT broker at {}", brokerUri);

        } catch (MqttException e) {
            log.error("Error initializing MQTT client: {}", e.getMessage(), e);
        }
    }

    private void handleReconnection(MqttConnectOptions options) {
        int attempt = 0;
        while (attempt < MAX_RECONNECT_ATTEMPTS) {
            try {
                Thread.sleep(BASE_RECONNECT_DELAY_MS * (1 << attempt)); // Exponential backoff
                client.connect(options);
                client.subscribe(registrationTopic);
                log.info("Reconnected to broker");
                return;
            } catch (MqttException | InterruptedException e) {
                attempt++;
                log.error("Reconnection attempt {} failed: {}", attempt, e.getMessage(), e);
                if (attempt >= MAX_RECONNECT_ATTEMPTS) {
                    log.error("Max reconnection attempts reached. Giving up.");
                }
            }
        }
    }
}