package com.mikayelovich.iot.control.mqttcontroler;


import com.mikayelovich.iot.control.model.commands.EnablePinForDuration;
import com.mikayelovich.iot.control.mqttcontroler.publisher.MQTTMessagePublisher;
import com.mikayelovich.iot.control.webcontroller.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendMqttMessageTest extends AbstractTest {


    @Autowired
    private MQTTMessagePublisher publisher;

    @Test
    public void test_send_mqtt_message() {
        EnablePinForDuration enablePinForDuration = new EnablePinForDuration(2, Duration.ofSeconds(3));
        enablePinForDuration.setUniqueId("a");
        publisher.sendCommand(enablePinForDuration);
    }


    @Test
    public void test_send_mqtt_message_multithreaded() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                EnablePinForDuration enablePinForDuration = new EnablePinForDuration(2, Duration.ofSeconds(3));
                enablePinForDuration.setUniqueId("a");
                publisher.sendCommand(enablePinForDuration);
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            // Wait for all threads to finish
        }
    }
}
