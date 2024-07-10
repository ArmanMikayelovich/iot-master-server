package com.mikayelovich.iot.control.model.dto.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestDTO {
    //unique device id can be mac address of the device, or mqtt broker, which that device listens
    @NotNull(message = "unique id  is required")
    private String uniqueDeviceId;
}
