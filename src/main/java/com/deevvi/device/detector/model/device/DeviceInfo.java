package com.deevvi.device.detector.model.device;

import com.deevvi.device.detector.engine.parser.Parser;

/**
 * POJO class that holds all information about a device.
 */
public final class DeviceInfo {

    /**
     * Device brand.
     */
    private final String brand;

    /**
     * Device type.
     */
    private final String device;

    /**
     * Device model.
     */
    private final String model;

    /**
     * Constructor.
     */
    public DeviceInfo(String brand, String device, String model) {

        this.brand = brand;
        this.device = device;
        this.model = Parser.clear(model);
    }

    public String getModel() {
        return model;
    }

    public String getDevice() {
        return device;
    }

    public String getBrand() {
        return brand;
    }
}
