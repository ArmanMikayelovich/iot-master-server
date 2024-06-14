package com.mikayelovich.iot.control.webcontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class IotWebControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotWebControlApplication.class, args);
    }

}
