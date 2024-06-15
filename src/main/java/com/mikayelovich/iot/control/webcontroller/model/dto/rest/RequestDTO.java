package com.mikayelovich.iot.control.webcontroller.model.dto.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestDTO {
    @NotNull(message = "mac address is required")
    private String macAddress;
}
