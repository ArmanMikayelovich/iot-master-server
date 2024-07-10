package com.mikayelovich.iot.control.model.dto.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PinModeCommandRequestDto extends RequestDTO {
    @NotNull(message = "Pin Number is required")
    private Integer pinNumber;

    @NotNull(message = "pinModeState is required")
    private String pinModeState;

}
