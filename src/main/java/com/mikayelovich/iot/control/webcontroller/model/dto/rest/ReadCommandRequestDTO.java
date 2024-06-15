package com.mikayelovich.iot.control.webcontroller.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReadCommandRequestDTO extends RequestDTO {
    @NotNull(message = "Pin Number is required")
    private Integer pinNumber;
}
