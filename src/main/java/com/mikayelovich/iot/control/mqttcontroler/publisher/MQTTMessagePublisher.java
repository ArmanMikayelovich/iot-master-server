package com.mikayelovich.iot.control.mqttcontroler.publisher;

import com.mikayelovich.iot.control.model.commands.abstraction.Command;
import com.mikayelovich.iot.control.mqttcontroler.StateMachine;
import com.mikayelovich.iot.control.mqttcontroler.events.MqttPublishedEvent;
import com.mikayelovich.iot.control.webcontroller.connectors.CommandPublisher;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

@Service
@Slf4j
public class MQTTMessagePublisher implements CommandPublisher<String> {
    private static final String MQTT_CLIENT_ID = "message_sender";
    private static final String PROTOCOL = "tcp://";
    private static final int QOS = 2;
    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final int BASE_RECONNECT_DELAY_MS = 1000;

    @Value("${mqtt.broker.host}")
    private String mqttBrokerHostUri;
    @Value("${mqtt.broker.port}")
    private String port;
    @Value("${mqtt.broker.username}")
    private String mqttUsername;
    @Value("${mqtt.broker.password}")
    private String mqttPassword;
    @Value("${mqtt.broker.pool-size}")
    private Integer maxPoolSize;

    @Autowired
    private StateMachine stateMachine;

    private String brokerPath;
    private Semaphore semaphore;
    private final ConcurrentLinkedQueue<MqttClient> clientPool = new ConcurrentLinkedQueue<>();

    @PostConstruct
    public void init() {
        brokerPath = PROTOCOL + mqttBrokerHostUri + ":" + port;
        semaphore = new Semaphore(maxPoolSize, true);
        initializeClientPool();
    }

    private void initializeClientPool() {
        for (int i = 0; i < maxPoolSize; i++) {
            MqttClient client = createAndConnectClient();
            clientPool.add(client);
        }
    }

    @Override
    public void sendCommand(Command<String> command) {
        MqttClient client = null;
        try {
            semaphore.acquire();
            client = getClientFromPool();
            String content = command.mapToExecutable();
            String topic = command.getUniqueId();

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(QOS);

            client.publish(topic, message);
            log.debug("Message sent: {}", message);
            addMqttEventIntoStateMachine(command, topic);
        } catch (MqttException e) {
            log.error("Error sending MQTT message", e);
            throw new RuntimeException("Error sending MQTT message", e);
        } catch (InterruptedException e) {
            log.error("Semaphore acquire interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            if (client != null) {
                releaseClientToPool(client);
            }
            semaphore.release();
        }

    }

    private void addMqttEventIntoStateMachine(Command<String> command, String topic) {
        MqttPublishedEvent mqttPublishedEvent = new MqttPublishedEvent(topic, command.mapToExecutable());
        stateMachine.registerEvent(topic, mqttPublishedEvent);
    }

    private MqttClient createAndConnectClient() {
        try {
            MqttClient client = new MqttClient(brokerPath, MQTT_CLIENT_ID + "-" + System.currentTimeMillis());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(mqttUsername);
            connOpts.setPassword(mqttPassword.toCharArray());

            log.debug("Connecting to broker: {}", brokerPath);
            client.connect(connOpts);
            log.debug("Connected");

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("Connection lost", cause);
                    handleReconnection(client);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    // Handle incoming messages if needed
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Handle delivery completion if needed
                }
            });

            return client;
        } catch (MqttException e) {
            log.error("Error creating MQTT client", e);
            throw new RuntimeException("Error creating MQTT client", e);
        }
    }

    private void handleReconnection(MqttClient client) {
        int attempt = 0;
        while (attempt < MAX_RECONNECT_ATTEMPTS) {
            try {
                Thread.sleep( BASE_RECONNECT_DELAY_MS * (1L << attempt)); // Exponential backoff
                client.reconnect();
                log.debug("Reconnected to broker");
                return;
            } catch (MqttException | InterruptedException e) {
                attempt++;
                log.error("Reconnection attempt {} failed", attempt, e);
                if (attempt >= MAX_RECONNECT_ATTEMPTS) {
                    log.error("Max reconnection attempts reached. Giving up.");
                }
            }
        }
    }

    private MqttClient getClientFromPool() {
        MqttClient client = clientPool.poll();
        if (client == null) {
            throw new RuntimeException("No available MQTT clients in the pool");
        }
        return client;
    }

    private void releaseClientToPool(MqttClient client) {
        clientPool.add(client);
    }


    @Override
    public void close() {
        clientPool.forEach(client -> {
            try {
                client.disconnect();
                client.close();
            } catch (MqttException e) {
                log.error("Error disconnecting MQTT client", e);
            }
        });
    }
}