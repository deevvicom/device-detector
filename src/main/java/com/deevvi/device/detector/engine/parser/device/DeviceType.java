package com.deevvi.device.detector.engine.parser.device;

/**
 * Enum containing all types of devices.
 */
public enum DeviceType {

    CAMERA("camera"),
    CAR_BROWSER("car browser"),
    CONSOLE("console"),
    DESKTOP("desktop"),
    FEATURE_PHONE("feature phone"),
    PORTABLE_MEDIA_PLAYER("portable media player"),
    PHABLET("phablet"),
    SMARTPHONE("smartphone"),
    TV("tv"),
    TABLET("tablet");

    /**
     * Device name.
     */
    private final String deviceName;

    /**
     * Constructor.
     */
    DeviceType(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * Get device name, in internal representation.
     *
     * @return device name
     */
    public String getDeviceName() {
        return deviceName;
    }
}
