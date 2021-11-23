package com.deevvi.device.detector.model.device;

import com.deevvi.device.detector.model.Model;

import java.util.List;
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
    public Device(String rawRegex, String device, String model, String brand, List<Model> models) {

        super(rawRegex, device, brand, models);
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}
