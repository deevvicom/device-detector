package com.deevvi.device.detector.model.device;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Model for camera parser.
 */
public class Device extends BasicDeviceWithModels {

    private final String model;

    /**
     * Constructor.
     */
    public Device(Pattern pattern, String device, String model, String brand, Map<Pattern, String> models) {

        super(pattern, device, brand, models);
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}
