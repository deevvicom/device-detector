package com.deevvi.device.detector.model.device;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Base model for any client.
 */
public class BasicDevice {

    /**
     * Compiled regex user to determine a device.
     */
    private final Pattern pattern;

    /**
     * Device name.
     */
    private final String device;

    /**
     * Device brand.
     */
    private final String brand;

    /**
     * Constructor.
     */
    public BasicDevice(Pattern pattern, String device, String brand) {

        Preconditions.checkNotNull(pattern, "Regex pattern cannot be null.");
        Preconditions.checkNotNull(StringUtils.trimToNull(brand), "Brand cannot be null or empty.");

        this.pattern = pattern;
        this.device = device;
        this.brand = brand;
    }

    /**
     * Get pattern.
     *
     * @return pattern used to detect a device
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Get device name.
     *
     * @return device name
     */
    public String getDevice() {
        return device;
    }

    /**
     * Get device brand.
     *
     * @return device brand
     */
    public String getBrand() {
        return brand;
    }
}
