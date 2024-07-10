package com.mikayelovich.iot.control.model.microcontrontroller.esp32;

import lombok.Getter;

@Getter
public enum Esp32Devkit1Pin {
    GPIO0(0, PinMode.INPUT_OUTPUT, "outputs PWM signal at boot, must be LOW to enter flashing mode"),
    GPIO1(1, PinMode.INPUT_OUTPUT, "debug output at boot"),
    GPIO2(2, PinMode.INPUT_OUTPUT, "connected to on-board LED, must be left floating or LOW to enter flashing mode"),
    GPIO3(3, PinMode.INPUT_OUTPUT, "HIGH at boot"),
    GPIO4(4, PinMode.INPUT_OUTPUT, ""),
    GPIO5(5, PinMode.INPUT_OUTPUT, "outputs PWM signal at boot, strapping pin"),
    GPIO6(6, PinMode.INPUT_ONLY, "connected to the integrated SPI flash"),
    GPIO7(7, PinMode.INPUT_ONLY, "connected to the integrated SPI flash"),
    GPIO8(8, PinMode.INPUT_ONLY, "connected to the integrated SPI flash"),
    GPIO9(9, PinMode.INPUT_ONLY, "connected to the integrated SPI flash"),
    GPIO10(10, PinMode.INPUT_ONLY, "connected to the integrated SPI flash"),
    GPIO11(11, PinMode.INPUT_ONLY, "connected to the integrated SPI flash"),
    GPIO12(12, PinMode.INPUT_OUTPUT, "boot fails if pulled high, strapping pin"),
    GPIO13(13, PinMode.INPUT_OUTPUT, ""),
    GPIO14(14, PinMode.INPUT_OUTPUT, "outputs PWM signal at boot"),
    GPIO15(15, PinMode.INPUT_OUTPUT, "outputs PWM signal at boot, strapping pin"),
    GPIO16(16, PinMode.INPUT_OUTPUT, ""),
    GPIO17(17, PinMode.INPUT_OUTPUT, ""),
    GPIO18(18, PinMode.INPUT_OUTPUT, ""),
    GPIO19(19, PinMode.INPUT_OUTPUT, ""),
    GPIO21(21, PinMode.INPUT_OUTPUT, ""),
    GPIO22(22, PinMode.INPUT_OUTPUT, ""),
    GPIO23(23, PinMode.INPUT_OUTPUT, ""),
    GPIO25(25, PinMode.INPUT_OUTPUT, ""),
    GPIO26(26, PinMode.INPUT_OUTPUT, ""),
    GPIO27(27, PinMode.INPUT_OUTPUT, ""),
    GPIO32(32, PinMode.INPUT_OUTPUT, ""),
    GPIO33(33, PinMode.INPUT_OUTPUT, ""),
    GPIO34(34, PinMode.INPUT_ONLY, ""),
    GPIO35(35, PinMode.INPUT_ONLY, ""),
    GPIO36(36, PinMode.INPUT_ONLY, ""),
    GPIO39(39, PinMode.INPUT_ONLY, "");

    private final int pinNumber;
    private final PinMode pinMode;
    private final String notes;

    Esp32Devkit1Pin(int pinNumber, PinMode pinMode, String notes) {
        this.pinNumber = pinNumber;
        this.pinMode = pinMode;
        this.notes = notes;
    }

    public enum PinMode {
        INPUT_ONLY,
        OUTPUT_ONLY,
        INPUT_OUTPUT
    }

    @Override
    public String toString() {
        return String.valueOf(pinNumber);
    }

    public static Esp32Devkit1Pin fromPinNumber(int pinNumber) {
        for (Esp32Devkit1Pin value : Esp32Devkit1Pin.values()) {
            if (value.pinNumber == pinNumber) {
                return value;
            }
        }
        throw new IllegalArgumentException("no pin with number: " + pinNumber);
    }
}

