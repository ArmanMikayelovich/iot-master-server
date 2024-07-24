package com.mikayelovich.iot.control.mqttcontroler;

import com.mikayelovich.iot.control.mqttcontroler.listener.MqttMessageHandlerService;
import com.mikayelovich.iot.control.webcontroller.AbstractTest;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

import static org.assertj.core.api.Assertions.assertThat;

public class MqttListenerIntegrationTest extends AbstractTest {

    @Value("${mqtt.broker.host}")
    private String mqttBrokerHost;

    @Value("${mqtt.broker.port}")
    private int mqttBrokerPort;

    @Value("${mqtt.broker.username}")
    private String mqttBrokerUsername;

    @Value("${mqtt.broker.password}")
    private String mqttBrokerPassword;

    @Autowired
    private MqttMessageHandlerService mqttMessageHandlerService;

    private static MqttConnectOptions options;

    @DynamicPropertySource
    static void mqttProperties(DynamicPropertyRegistry registry) {
        registry.add("mqtt.broker.host", () -> "localhost");
        registry.add("mqtt.broker.port", () -> 1883);
        registry.add("mqtt.broker.username", () -> "esp8266");
        registry.add("mqtt.broker.password", () -> "esp8266");
        registry.add("mqtt.registration-topic", () -> "registration");
    }

    @BeforeAll
    public static void setUp() {
        options = new MqttConnectOptions();
        options.setUserName("esp8266");
        options.setPassword("esp8266".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(60);
    }

    @Test
    public void testMessageConsumption() throws MqttException, InterruptedException {
        String brokerUrl = "tcp://" + mqttBrokerHost + ":" + mqttBrokerPort;
        MqttClient client = new MqttClient(brokerUrl, MqttClient.generateClientId());

        client.connect(options);

        String testMessage = "test-message";
        MqttMessage message = new MqttMessage(testMessage.getBytes());
        client.publish("registration", message);

        Thread.sleep(2000);

        assertThat(mqttMessageHandlerService.getLastHandledMessage()).isEqualTo(testMessage);

        client.disconnect();
    }
}
