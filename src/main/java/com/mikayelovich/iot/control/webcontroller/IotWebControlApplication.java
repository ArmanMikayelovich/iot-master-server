package com.mikayelovich.iot.control.webcontroller;

import com.mikayelovich.iot.control.webcontroller.connectors.mqtt.MQTTMessagePublisher;
import com.mikayelovich.iot.control.webcontroller.model.commands.EnablePinForDuration;
import com.mikayelovich.iot.control.webcontroller.model.microcontrontroller.esp32.Esp32Devkit1Pin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;

@Slf4j
@SpringBootApplication
public class IotWebControlApplication implements CommandLineRunner {

    @Autowired
    private MQTTMessagePublisher publisher;

    public static void main(String[] args) {
        SpringApplication.run(IotWebControlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        EnablePinForDuration enablePinForDuration = new EnablePinForDuration(Esp32Devkit1Pin.GPIO2, Duration.ofSeconds(3));
        publisher.sendCommand(enablePinForDuration);
    }
}
