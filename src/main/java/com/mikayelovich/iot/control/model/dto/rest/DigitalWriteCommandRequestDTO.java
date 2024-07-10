package com.mikayelovich.iot.control.model.dto.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class DigitalWriteCommandRequestDTO extends RequestDTO {
    @NotNull(message = "Pin Number is required")
    private Integer pinNumber;

    @NotNull(message = "state is required")
    private String state;

}
