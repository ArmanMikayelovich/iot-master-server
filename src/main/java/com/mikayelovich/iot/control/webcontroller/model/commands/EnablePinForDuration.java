package com.mikayelovich.iot.control.webcontroller.model.commands;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EnablePinForDuration extends DigitalWriteCommand {
    private final static String ENABLE_PIN_FOR = "enablePinFor";

    private final long durationInSeconds;


    public EnablePinForDuration(int pinNumber, Duration duration) {
        super(pinNumber, DigitalWriteState.HIGH);
        this.durationInSeconds = duration.toSeconds();
    }

    @Override
    protected String getCommandName() {
        return ENABLE_PIN_FOR;
    }

    @Override
    protected List<String> getParameters() {
        ArrayList<String> params = new ArrayList<>(super.getParameters());
        params.add(String.valueOf(durationInSeconds));
        return params;
    }
}
