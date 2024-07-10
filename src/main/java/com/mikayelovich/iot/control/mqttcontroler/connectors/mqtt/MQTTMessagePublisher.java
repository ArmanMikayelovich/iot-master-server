package com.mikayelovich.iot.control.mqttcontroler.connectors.mqtt;

import com.mikayelovich.iot.control.webcontroller.connectors.CommandPublisher;
import com.mikayelovich.iot.control.model.commands.abstraction.Command;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Slf4j
public class MQTTMessagePublisher implements CommandPublisher<String> {

    private static final String MQTT_CLIENT_ID = "RegistrationListener";

    private static final int QOS = 2;
    @Value("${mqtt.broker.host}")
    private String mqttBrokerHostUri;
    @Value("${mqtt.broker.port}")
    private String port;

    private MqttClient client;

    @PostConstruct
    public void init() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void sendCommand(Command<String> command) {
        String broker = "tcp://" + mqttBrokerHostUri + ":" + port;
        try {
            client = new MqttClient(broker, MQTT_CLIENT_ID);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("esp8266");
            connOpts.setPassword("esp8266".toCharArray());

            System.out.println("Connecting to broker: " + broker);

            client.connect(connOpts);

            System.out.println("Connected");

            String content = command.mapToExecutable();  // Message content
            String topic = "topic";  // Replace with your topic

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(QOS);

            client.publish(topic, message);
            log.debug("message send: {}", message);

        } catch (MqttException me) {
            log.error("some error occurred", me);
            log.error("reason " + me.getReasonCode());
            log.error("msg " + me.getMessage());
            log.error("loc " + me.getLocalizedMessage());
            log.error("cause " + me.getCause());
            log.error("excep " + me);
            throw new RuntimeException(me);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            client.close();
        } catch (MqttException e) {
            log.error("error while closing mqtt client", e);
        }
    }
}
