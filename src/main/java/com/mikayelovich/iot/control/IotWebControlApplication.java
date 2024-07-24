package com.mikayelovich.iot.control;

import com.mikayelovich.iot.control.mqttcontroler.publisher.MQTTMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class IotWebControlApplication{

    @Autowired
    private MQTTMessagePublisher publisher;

    public static void main(String[] args) {
        SpringApplication.run(IotWebControlApplication.class, args);
    }

}
